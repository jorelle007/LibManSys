
package libmansys.model;

public class User {  
    private String username;
    private String password;
    private String full_name;
    private String role;
    private String security_question;
    private String security_answer;
    
    public User() {

    }
    
    
    //Getters and Setters    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
        
    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getSecurity_question() { return security_question; }
    public void setSecurity_question(String secret_question) { this.security_question = secret_question; }
    
    public String getSecurity_answer() { return security_answer; }
    public void setSecurity_answer(String secret_answer) { this.security_answer = secret_answer; }
}
