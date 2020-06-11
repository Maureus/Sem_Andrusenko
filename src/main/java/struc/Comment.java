package struc;

public class Comment {
    private int commentID, smID;
    private String obsah, userLogin;

    public Comment(int commentID, int smID, String obsah, String userLogin) {
        this.commentID = commentID;
        this.smID = smID;
        this.obsah = obsah;
        this.userLogin = userLogin;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getSmID() {
        return smID;
    }

    public void setSmID(int smID) {
        this.smID = smID;
    }

    public String getObsah() {
        return obsah;
    }

    public void setObsah(String obsah) {
        this.obsah = obsah;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return userLogin+": " + obsah;
    }
}
