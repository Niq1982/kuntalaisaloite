package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.VerifiedUser;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.QAdminUser;
import fi.om.municipalityinitiative.sql.QVerifiedAuthor;
import fi.om.municipalityinitiative.sql.QVerifiedUser;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QVerifiedUser.*;

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
    public VerifiedUser getVerifiedUser(String hash) {
        return queryFactory.from(verifiedUser)
                .where(verifiedUser.hash.eq(hash))
                .uniqueResult(verifiedUserMapper);
    }

    @Override
    public VerifiedUserId addVerifiedUser(String hash, ContactInfo contactInfo) {
        return new VerifiedUserId(queryFactory.insert(verifiedUser)
                .set(verifiedUser.hash, hash)
                .set(verifiedUser.address, contactInfo.getAddress())
                .set(verifiedUser.name, contactInfo.getName())
                .set(verifiedUser.phone, contactInfo.getPhone())
                        //.set(verifiedUser.email, contactInfo.getEmail())
                .executeWithKey(verifiedUser.id));
    }


    private static Expression<User> omUserMapper = new MappingProjection<User>(User.class,
            QAdminUser.adminUser.name) {
        @Override
        protected User map(Tuple row) {
            return User.omUser(row.get(QAdminUser.adminUser.name));
        }
    };

    private static Expression<VerifiedUser> verifiedUserMapper = new MappingProjection<VerifiedUser>(VerifiedUser.class,
            verifiedUser.all()) {
        @Override
        protected VerifiedUser map(Tuple row) {
            VerifiedUser verifiedUser = new VerifiedUser();
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(row.get(QVerifiedUser.verifiedUser.phone));
            contactInfo.setName(row.get(QVerifiedUser.verifiedUser.name));
            contactInfo.setAddress(row.get(QVerifiedUser.verifiedUser.address));
            // contactInfo.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            verifiedUser.setContactInfo(contactInfo);
            verifiedUser.setUserId(new VerifiedUserId(row.get(QVerifiedUser.verifiedUser.id)));
            return verifiedUser;
        }
    };

}
