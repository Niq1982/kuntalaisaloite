package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.Maybe;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;

import static fi.om.municipalityinitiative.dao.JdbcInitiativeDao.assertSingleAffection;
import static fi.om.municipalityinitiative.sql.QVerifiedUser.verifiedUser;

@SQLExceptionTranslated
public class JdbcUserDao implements UserDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public User getAdminUser(String userName, String password) {

        User user = queryFactory.from(QAdminUser.adminUser)
                .where(QAdminUser.adminUser.username.eq(userName))
                .where(QAdminUser.adminUser.password.eq(password))
                .uniqueResult(omUserMapper);
        if (user == null) {
            throw new InvalidLoginException("Invalid login credentials for user " + userName);
        }
        return user;
    }

    @Override
    // TODO: Argh. Improve usage of QueryDSL
    public Maybe<VerifiedUser> getVerifiedUser(String hash) {

        class VerifiedUserDataWrapper {

            ContactInfo contactInfo;
            Maybe<Municipality> municipalityMaybe;
            VerifiedUserId verifiedUserId;
        }

        Maybe<VerifiedUserDataWrapper> userDataMaybe = Maybe.fromNullable(
                queryFactory.from(verifiedUser)
                        .leftJoin(verifiedUser.verifiedUserMunicipalityFk, QMunicipality.municipality)
                        .where(verifiedUser.hash.eq(hash))
                        .uniqueResult(new MappingProjection<VerifiedUserDataWrapper>(VerifiedUserDataWrapper.class,
                                QVerifiedUser.verifiedUser.all(),
                                QMunicipality.municipality.all()
                        ) {
                            @Override
                            protected VerifiedUserDataWrapper map(Tuple row) {
                                VerifiedUserDataWrapper verifiedUserDataWrapper = new VerifiedUserDataWrapper();
                                verifiedUserDataWrapper.contactInfo = new ContactInfo();
                                verifiedUserDataWrapper.contactInfo.setEmail(row.get(QVerifiedUser.verifiedUser.email));
                                verifiedUserDataWrapper.contactInfo.setShowName(true); // XXX: This is not needed, therefore ugly
                                verifiedUserDataWrapper.contactInfo.setAddress(row.get(QVerifiedUser.verifiedUser.address));
                                verifiedUserDataWrapper.contactInfo.setName(row.get(QVerifiedUser.verifiedUser.name));
                                verifiedUserDataWrapper.contactInfo.setPhone(row.get(QVerifiedUser.verifiedUser.phone));
                                verifiedUserDataWrapper.verifiedUserId = new VerifiedUserId(row.get(QVerifiedUser.verifiedUser.id));
                                if (row.get(QVerifiedUser.verifiedUser.municipalityId) == null) {
                                    verifiedUserDataWrapper.municipalityMaybe = Maybe.absent();
                                }
                                else {
                                    verifiedUserDataWrapper.municipalityMaybe = Maybe.of(new Municipality(
                                            row.get(QMunicipality.municipality.id),
                                            row.get(QMunicipality.municipality.name),
                                            row.get(QMunicipality.municipality.nameSv),
                                            row.get(QMunicipality.municipality.active)
                                    ));

                                }

                                return verifiedUserDataWrapper;
                            }
                        }));

        if (userDataMaybe.isNotPresent()) {
            return Maybe.absent();
        }


        // Get users initiatives
        List<Long> initiatives = queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QMunicipalityInitiative.municipalityInitiative._verifiedAuthorInitiativeFk, QVerifiedAuthor.verifiedAuthor)
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorVerifiedUserFk, QVerifiedUser.verifiedUser)
                .where(QVerifiedUser.verifiedUser.hash.eq(hash))
                .list(QMunicipalityInitiative.municipalityInitiative.id);

        return Maybe.of(User.verifiedUser(userDataMaybe.get().verifiedUserId, hash, userDataMaybe.get().contactInfo, new HashSet<>(initiatives), userDataMaybe.get().municipalityMaybe));
    }

    @Override
    public VerifiedUserId addVerifiedUser(String hash, ContactInfo contactInfo) {
        return new VerifiedUserId(queryFactory.insert(verifiedUser)
                .set(verifiedUser.hash, hash)
                .set(verifiedUser.address, contactInfo.getAddress())
                .set(verifiedUser.name, contactInfo.getName())
                .set(verifiedUser.phone, contactInfo.getPhone())
                .set(verifiedUser.email, contactInfo.getEmail())
                .setNull(verifiedUser.municipalityId)
                .executeWithKey(verifiedUser.id));
    }

    @Override
    public Maybe<VerifiedUserId> getVerifiedUserId(String hash) {
        Long maybeVerifiedUserId = queryFactory.from(verifiedUser)
                .where(verifiedUser.hash.eq(hash))
                .uniqueResult(verifiedUser.id);

        if (maybeVerifiedUserId == null) {
            return Maybe.absent();
        }
        return Maybe.of(new VerifiedUserId(maybeVerifiedUserId));

    }

    @Override
    public void updateUserInformation(String hash, ContactInfo contactInfo) {
        assertSingleAffection(queryFactory.update(verifiedUser)
                .set(verifiedUser.address, contactInfo.getAddress())
                .set(verifiedUser.email, contactInfo.getEmail())
                .set(verifiedUser.phone, contactInfo.getPhone())
                // NOTE: Do not update name for verified users, it's updated during login according to vetuma
                .where(verifiedUser.hash.eq(hash))
                .execute());
    }

    @Override
    public void updateUserInformation(String hash, String fullName, Maybe<Municipality> vetumaMunicipality) {
        SQLUpdateClause updateClause = queryFactory.update(verifiedUser)
                .set(verifiedUser.name, fullName)
                .where(verifiedUser.hash.eq(hash));

        if (vetumaMunicipality.isPresent()) {
            updateClause.set(verifiedUser.municipalityId, vetumaMunicipality.get().getId()); // XXX: What if is not found? Should be checked at service-layer?
        }
        else {
            updateClause.setNull(verifiedUser.municipalityId);
        }

        assertSingleAffection(updateClause.execute());
    }

    private static Expression<User> omUserMapper = new MappingProjection<User>(User.class,
            QAdminUser.adminUser.name) {
        @Override
        protected User map(Tuple row) {
            return User.omUser(row.get(QAdminUser.adminUser.name));
        }
    };

}
