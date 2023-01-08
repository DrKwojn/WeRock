package fri.werock.models;

public class User {
    private int id;

    private String username;

    private String email;

    private String fullname;

    private String description;

    private String tags;

    private String youtubekey;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getYoutubeKey() {
        return youtubekey;
    }

    public void setYoutubeKey(String youtubekey) {
        this.youtubekey = youtubekey;
    }
}
