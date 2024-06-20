import java.util.*;
import java.text.SimpleDateFormat;

import java.util.Date;
class User {
    int userId;
    String pass;
    String username;
    String gender;
    int age;
    List<String> hobbies;
    List<String> thoughtsList;
    List<String> timestamps;
    public User(int userId,String pass, String username, String gender, int age, List<String> hobbies) {//constructor
        this.userId = userId;
        this.pass=pass;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.hobbies = hobbies;
        thoughtsList = new ArrayList<>();
        timestamps = new ArrayList<>();
    }
    public void addThought(String thought) {
        thoughtsList.add(thought);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();//

        String timestamp = dateFormat.format(date);//mhnje yyyy-MM-dd HH:mm:ss madhe mala maji date pahije
        timestamps.add(timestamp);
    }

    public List<String> getThoughtsList() {

        return thoughtsList;
    }

    public List<String> getTimestamps() {

        return timestamps;
    }
}
class SocialNetwork {
    Map<Integer, User> users;

    List<List<User>> graph;//graph//arraylist of arraylist//adjlist
    public SocialNetwork() {
        users = new HashMap<>();
        graph = new ArrayList<>();
    }

    public void addUser(User user) {
        users.put(user.userId, user);
        while (graph.size() <= user.userId) {

            graph.add(new ArrayList<>());
        }
        System.out.println("***Registration successful.***");
        System.out.println("--->>>Your User ID " +(user.userId));
    }

    public User getUser(int userId) {

        return users.get(userId);
    }
    public void addFriendship(User user1, User user2) {//linking friends
        while (graph.size() <= user1.userId || graph.size() <= user2.userId) {//exception
            graph.add(new ArrayList<>());
        }
        List<User> friends1 = graph.get(user1.userId);
        List<User> friends2 = graph.get(user2.userId);

        if (!friends1.contains(user2)) {
            friends1.add(user2);//graph.get(user1).add(user2)//connect
        }

        if (!friends2.contains(user1)) {
            friends2.add(user1);
            System.out.println(user1.username + " and " + user2.username + " are now friends :D !!");
        } else {
            System.out.println("You are already friends!");
        }
    }

    public List<User> seeFriendship(User user1) {//arraylist
        List<User> friends = graph.get(user1.userId);
        return friends;
    }
    public List<User> getMutualFriends(User user1, User user2) {
        List<User> friends1 = graph.get(user1.userId);
        List<User> friends2 = graph.get(user2.userId);

        List<User> mutualFriends = new ArrayList<>();
        for (User friend : friends1) {
            if (friends2.contains(friend)) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }
    public void removeFriendship(User user1, User user2) {

        List<User> friends1 = graph.get(user1.userId);
        List<User> friends2 = graph.get(user2.userId);

        if (friends1.contains(user2)) {
            friends1.remove(user2);
        }

        if (friends2.contains(user1)) {
            friends2.remove(user1);
            System.out.println("Friends Removed :{ !!");
            System.out.println(user1.username + " and " + user2.username + " are not friends now!");
        } else {
            System.out.println(user1.username + " and " + user2.username + " are not friends from the start.");
        }
    }

    public List<User> suggestFriends(User user) {
        List<User> suggestions = new ArrayList<>();
        int userAge = user.age;
        String userGender = user.gender;
        List<String> userHobbies = user.hobbies;
        List<User> friends = graph.get(user.userId);
        for (User potentialFriend : users.values()) {//for each
            if (potentialFriend != user) {
                int potentialFriendAge = potentialFriend.age;
                List<String> potentialFriendHobbies = potentialFriend.hobbies;
                if (Math.abs(potentialFriendAge - userAge) <= 5) {//Suggest Friend based on age
                    System.out.println("Friend Suggestion On Your Age: " + potentialFriend.username+" User Id: "+potentialFriend.userId);
                    suggestions.add(potentialFriend);
                }
                if (hasCommonHobbies(potentialFriendHobbies, userHobbies)) {//Suggest Friend based on hobby
                    System.out.println("Friend Suggestion On Your Hobbies: " + potentialFriend.username+" User Id: "+potentialFriend.userId);
                    suggestions.add(potentialFriend);
                }
            }
        }
        for (User friend : friends) {//Suggest Friend based on Friends of Friends
            List<User> friendsOfFriend = graph.get(friend.userId);
            for (User fof : friendsOfFriend) {
                if (!friends.contains(fof) && fof != user && !suggestions.contains(fof)) {
                    System.out.println(fof.username+"User id: "+fof.userId+" friends with "+friend.username);
                    suggestions.add(fof);
                }
            }
        }
        return suggestions;
    }
    private boolean hasCommonHobbies(List<String> hobbies1, List<String> hobbies2) {//helper function
        for (String hobby : hobbies1) {
            if (hobbies2.contains(hobby)) {
                return true;
            }
        }
        return false;
    }
    public void shareThoughts(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("^^^Enter your thoughts: ^^^");
        String thoughts = scanner.nextLine();
        user.addThought(thoughts);
        System.out.println("Thought added successfully!! :D <3");
    }

    public void displayThoughts(User user) {
        List<String> thoughts = user.getThoughtsList();
        List<String> timestamps = user.getTimestamps();

        for (int i = 0; i < thoughts.size(); i++) {//for (Integer i: thoughts)
            System.out.println("||||||||||||||||||||||||||||||||||||||");
            System.out.println("||Timestamp: " + timestamps.get(i)+" ||");
            System.out.println("||Thought: " + thoughts.get(i)+" ||");
            System.out.println("||||||||||||||||||||||||||||||||||||||");
        }
    }
    public void peekIntoAccount(User viewer, int userId) {
        User user = getUser(userId);
        if (user != null) {
            System.out.println("Username: " + user.username);
            System.out.println("Gender: " + user.gender);
            System.out.println("Age: " + user.age);
            System.out.println("Hobbies: " + String.join(", ", user.hobbies));//string function

            List<User> mutualFriends = getMutualFriends(user, viewer);
            if (mutualFriends.isEmpty()) {
                System.out.println("****You have no mutual friends with " + user.username+"****");
            } else {
                System.out.println("****Mutual Friends with " + viewer.username + ":****");
                for (User mutualFriend : mutualFriends) {
                    System.out.println("-->> "+mutualFriend.username);
                }
            }


            displayThoughts(user);
        } else {
            System.out.println("User not found.");
        }
    }


    public void listUsers() {
        //System.out.println("Current Users:");
        for (User user : users.values()) {
            System.out.println("User ID: " + user.userId );
            System.out.println("Username: "+ user.username);
            System.out.println("Gender: " + user.gender);
            System.out.println("Age: " + user.age);
            System.out.println("Hobbies: " + String.join(", ", user.hobbies));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocialNetwork network = new SocialNetwork();
        int userIdCounter = 1;


        while (true) {
            System.out.println("1. Sign In?");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) { // Sign Up
                System.out.print("Enter your username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String pass= scanner.nextLine();
                System.out.print("Enter your gender F-Female M-Male O-Others: ");
                String gender = scanner.nextLine();
                System.out.print("Enter your age: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter your hobbies (comma-separated): ");
                String[] hobbyArray = scanner.nextLine().split(",");//string function
                User newUser = new User(userIdCounter++,pass, username,gender,age,Arrays.asList(hobbyArray));
                network.addUser(newUser);

            } else if (choice == 2) { // Log In
                System.out.print("Enter your user ID: ");
                int userId = scanner.nextInt();
                scanner.nextLine();
                User user = network.getUser(userId);
                    if (user != null) {
                        System.out.print("Enter Password: ");
                        String passss=scanner.nextLine();
                        if(user.pass.equals(passss)){
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("  ********Welcome, " + user.username + "!********");
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("---------------------------------------------");
                            System.out.println("||Here are few friends suggestion for you!!||");
                            System.out.println("---------------------------------------------");
                            List<User> suggestions = network.suggestFriends(user);//Friend Suggestion
                            if (suggestions.isEmpty()) {
                                network.listUsers();
                            }
                            System.out.println("---------------------------------------------");
                            System.out.println("    ******Share Your Thoughts******");
                            System.out.println("--->>>Do you wish to share your thoughts<<<---");
                            System.out.println("1-Yes "+'\n'+"0-No");
                            int temp= scanner.nextInt();
                            if(temp==1){
                                network.shareThoughts(user);//Share Your Thoughts
                                network.displayThoughts(user);
                            }
                            int option;
                            do{
                                System.out.println("1. Add Friend");
                                System.out.println("2. See Your Friends");
                                System.out.println("                  -See Mutual Friends");
                                System.out.println("                  -Remove Friends");
                                System.out.println("3. Peek Into Account");//stalk
                                System.out.println("4. Log Out");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option == 1) { // Add Friend
                                    System.out.print("Enter the user ID of the friend you want to add: ");
                                    int friendId = scanner.nextInt();
                                    User friend = network.getUser(friendId);
                                    if (friend != null) {
                                        network.addFriendship(user, friend);
                                    } else {
                                        System.out.println("User not found.");
                                    }
                                }else if(option==2){//See Your Friends
                                    List<User> friends = network.seeFriendship(user);
                                    if(friends.isEmpty()){
                                        System.out.println("You have no friends");
                                    }else{
                                        System.out.println("Your Friend List: ");
                                        for(User frnd: friends) {//for each//for(datatype iterator:list)
                                            System.out.println(frnd.username);
                                        }
                                        System.out.println("Do you wish to Remove a friend or See Mutual friend");
                                        System.out.println("1-Yes "+'\n'+"0-No");
                                        int ch= scanner.nextInt();
                                        if(ch==1){
                                            System.out.println("1. See Mutual Friends"+'\n'+"2. Remove Friend");
                                            int a= scanner.nextInt();
                                            if(a==1){//See Mutuals
                                                System.out.print("Enter the user ID of the friend to check mutual friends with: ");
                                                int friendId = scanner.nextInt();
                                                User friend = network.getUser(friendId);
                                                if (friend != null) {
                                                    List<User> mutualFriends = network.getMutualFriends(user, friend);
                                                    if (mutualFriends.isEmpty()) {
                                                        System.out.println("You have no mutual friends with " + friend.username);
                                                    } else {
                                                        System.out.println("Mutual Friends with " + friend.username + ":");
                                                        for (User mutualFriend : mutualFriends) {
                                                            System.out.println(mutualFriend.username);
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("User not found.");
                                                }
                                            }
                                            if(a==2){//Remove Friends
                                                System.out.print("Enter the user ID of the friend: ");
                                                int friendId = scanner.nextInt();
                                                User friend = network.getUser(friendId);
                                                if (friend != null) {
                                                    network.removeFriendship(user, friend);
                                                } else {
                                                    System.out.println("User not found.");
                                                }
                                            }
                                        }
                                    }
                                }else if(option == 3){//Stalk Account
                                    System.out.print("Enter the user ID to peek into: ");
                                    int peekUserId = scanner.nextInt();
                                    User peekfrnd = network.getUser(peekUserId);
                                    if(peekfrnd != null){
                                        network.peekIntoAccount(user, peekUserId);
                                    }else{
                                        System.out.println("User not found.");
                                    }
                                }
                            }while(option!=4);

                        }else{
                            System.out.println("Incorrect Password");
                        }

                    } else {
                        System.out.println("User not found.");
                    }


            } else if (choice == 3) { // Exit
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}