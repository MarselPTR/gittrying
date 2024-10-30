import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SimpleInstagram extends JFrame implements ActionListener {
    private ArrayList<User> users;
    private ArrayList<Post> posts;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField captionField;
    private JTextField imageUrlField;
    private JTextArea feedArea;
    private JTextArea commentArea;
    private User currentUser;

    public SimpleInstagram() {
        users = new ArrayList<>();
        posts = new ArrayList<>();
        createUI();
    }

    private void createUI() {
        setTitle("Simple Instagram Clone");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // User registration and login panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(3, 2));

        userPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        userPanel.add(usernameField);

        userPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        userPanel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        userPanel.add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser());
        userPanel.add(loginButton);

        add(userPanel);

        // Post creation panel
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new GridLayout(3, 2));

        postPanel.add(new JLabel("Caption:"));
        captionField = new JTextField();
        postPanel.add(captionField);

        postPanel.add(new JLabel("Image URL:"));
        imageUrlField = new JTextField();
        postPanel.add(imageUrlField);

        JButton postButton = new JButton("Post");
        postButton.addActionListener(e -> createPost());
        postPanel.add(postButton);

        add(postPanel);

        // Feed area to display posts
        feedArea = new JTextArea(15, 40);
        feedArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(feedArea);
        add(scrollPane);

        // Comment area
        commentArea = new JTextArea(5, 40);
        commentArea.setEditable(true);
        add(new JLabel("Add Comment:"));
        add(commentArea);

        JButton commentButton = new JButton("Comment");
        commentButton.addActionListener(e -> addComment());
        add(commentButton);

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!username.isEmpty() && !password.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                    return;
                }
            }
            users.add(new User(username, password));
            JOptionPane.showMessageDialog(this, "User registered successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                JOptionPane.showMessageDialog(this, "Logged in successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid username or password.");
    }

    private void createPost() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to post.");
            return;
        }

        String caption = captionField.getText();
        String imageUrl = imageUrlField.getText();

        if (!caption.isEmpty() && !imageUrl.isEmpty()) {
            Post newPost = new Post(caption, imageUrl, currentUser);
            posts.add(newPost);
            displayPosts();
            captionField.setText("");
            imageUrlField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void addComment() {
        String comment = commentArea.getText();

        if (!comment.isEmpty() && !posts.isEmpty()) {
            Post lastPost = posts.get(posts.size() - 1); // Add comment to the last post
            lastPost.addComment(new Comment(comment, currentUser));
            commentArea.setText("");
            displayPosts();
        } else {
            JOptionPane.showMessageDialog(this, "Please write a comment.");
        }
    }

    private void displayPosts() {
        feedArea.setText("");
        for (Post post : posts) {
            feedArea.append(post.getUsername() + ": " + post.getCaption() + "\n");
            feedArea.append("Image URL: " + post.getImageUrl() + "\n");
            for (Comment comment : post.getComments()) {
                feedArea.append("    Comment by " + comment.getUsername() + ": " + comment.getText() + "\n");
            }
            feedArea.append("\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleInstagram::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // Class to represent a User
    class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    // Class to represent a Post
    class Post {
        private String caption;
        private String imageUrl;
        private User user;
        private ArrayList<Comment> comments;

        public Post(String caption, String imageUrl, User user) {
            this.caption = caption;
            this.imageUrl = imageUrl;
            this.user = user;
            this.comments = new ArrayList<>();
        }

        public void addComment(Comment comment) {
            comments.add(comment);
        }

        public String getCaption() {
            return caption;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUsername() {
            return user.getUsername();
        }

        public ArrayList<Comment> getComments() {
            return comments;
        }
    }

    // Class to represent a Comment
    class Comment {
        private String text;
        private User user;

        public Comment(String text, User user) {
            this.text = text;
            this.user = user;
        }

        public String getText() {
            return text;
        }

        public String getUsername() {
            return user.getUsername();
        }
    }
}
