package qywx;

import java.util.Objects;

/**
 * @author huqi
 **/
public class UserModel {

    private String userid;
    private String mobile;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserModel userModel = (UserModel) o;
        return Objects.equals(userid, userModel.userid) &&
                Objects.equals(mobile, userModel.mobile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userid, mobile);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userid='" + userid + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
