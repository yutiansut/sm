package cn.mdni.core.shiro;

import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

/**
 * <dl>
 * <dd>Description: 密码加密工具类</dd>
 * <dd>Company: 美得你信息技术有限公司</dd>
 * <dd>@date：2017/3/7 16:21</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class PasswordUtil {
    private static final int SALT_SIZE = 8;
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_ITERATIONS = 1024;

    /**
     * 普通用户密码加密
     *
     * @param password 原始密码
     * @param salt     盐值
     * @return
     */
    public static String entryptUserPassword(String password, String salt) {
        return hashPassword(password, salt);
    }

    public static String generateSalt() {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        return Encodes.encodeHex(salt);
    }

    public static String hashPassword(String plainPassword, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), HASH_ITERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

    /**
     * @param loginPassword 登录时，传入的明文密码
     */
//    public static boolean isPasswordCorrect(String loginPassword, User user) {
//        return PasswordUtil.hashPassword(loginPassword, user.getSalt()).equals(user.getPassWord());
//    }

}
