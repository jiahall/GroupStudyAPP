package android.jia.groupstudy;

public class User {


    String banned;
    String member;


    public User() {
    }


    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public User(String uid, String banned, String member) {

        this.banned = banned;
        this.member = member;
    }
}
