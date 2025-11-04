
package libmansys.model;

/**
 *
 * @author jorel
 */
public class UserModel {
    private int user_id;    
    private String username;
    private String password;
    private String full_name;
    private String role;
    private String secret_question;
    private String secret_answer;
    
    public UserModel() {

    }
    
    
    //Getters and Setters
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
        
    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getSecret_question() { return secret_question; }
    public void setSecret_question(String secret_question) { this.secret_question = secret_question; }
    
    public String getSecret_answer() { return secret_answer; }
    public void setSecret_answer(String secret_answer) { this.secret_answer = secret_answer; }
}
