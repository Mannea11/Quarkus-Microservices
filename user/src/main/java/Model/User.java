package Model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

    public class User {
        @Id
        @Column(nullable = false)
        public String username;

        @Column(nullable = false)
        public String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

    public User() {
    }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
