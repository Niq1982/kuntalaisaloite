package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.QAdminUser;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QVerifiedAuthor;
import fi.om.municipalityinitiative.sql.QVerifiedUser;
import fi.om.municipalityinitiative.util.Maybe;

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
    public Maybe<VerifiedUser> getVerifiedUser(String hash) {
        Maybe<ContactInfo> contactInfoMaybe = Maybe.fromNullable(queryFactory.from(verifiedUser)
                .where(verifiedUser.hash.eq(hash))
                .uniqueResult(Mappings.verifiedUserContactInfo));

        if (contactInfoMaybe.isNotPresent()) {
            return Maybe.absent();
        }

        // Get users initiatives
        List<Long> initiatives = queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QMunicipalityInitiative.municipalityInitiative._verifiedAuthorInitiativeFk, QVerifiedAuthor.verifiedAuthor)
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorVerifiedUserFk, QVerifiedUser.verifiedUser)
                .where(QVerifiedUser.verifiedUser.hash.eq(hash))
                .list(QMunicipalityInitiative.municipalityInitiative.id);

        return Maybe.of(User.verifiedUser(hash, contactInfoMaybe.get(), new HashSet<>(initiatives)));
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
                .where(verifiedUser.hash.eq(hash))
                .execute());
    }

    private static Expression<User> omUserMapper = new MappingProjection<User>(User.class,
            QAdminUser.adminUser.name) {
        @Override
        protected User map(Tuple row) {
            return User.omUser(row.get(QAdminUser.adminUser.name));
        }
    };

}
