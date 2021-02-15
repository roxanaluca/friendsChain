
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class User {
    String name;
    List<String> friends;
    public User(String name){
        this.name = name;
        friends = new LinkedList<String>();
    }

    List<String> getFriends()
    {
        return this.friends;
    }

    public String getName(){
        return this.name;
    }
}
class InputParams{
    public HashMap<String, User> people;
    public String userA, userB;

    public InputParams(HashMap<String, User> people, String userA, String userB)
    {
        this.people = people;
        this.userA = userA;
        this.userB = userB;
    }

}

class UserQueue{
    public HashMap<String, User> isVisited;
    public Queue<User> queue;

    public UserQueue(User start){
        queue = new LinkedList<>();
        isVisited = new HashMap<>();
        queue.add(start);
        isVisited.put(start.getName(),null);
    }

    public boolean hasNext(){
        return !this.queue.isEmpty();
    }
}

class Solution {
    public static void main(String[] args) throws IOException {
        InputParams network = readUsers();
        List<String> result = getChainOfFriends(network);
        System.out.println(result.stream().collect(Collectors.joining(" ")));
    }

    public static InputParams readUsers() throws IOException {
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        String[] memberNames =  input.readLine().split(" ");
        int numberOfUsers = memberNames.length;
        HashMap<String,User> network = new HashMap<>();
        for (int i=0;i< numberOfUsers;i++)
            network.put(memberNames[i], new User(memberNames[i]));
        String[] users = input.readLine().split(" ");

        while (true)
        {
            String line = input.readLine();
            if (line== null) break;
            String[] link = line.split(" ");
            int userAId = Integer.parseInt(link[0]),userBId = Integer.parseInt(link[1]);
            User auxA, auxB;
            auxA = network.get(memberNames[userAId]);
            auxB = network.get(memberNames[userBId]);
            if (!auxA.getFriends().contains(auxA) && !auxB.getFriends().contains(auxB))
            {
                auxA.getFriends().add(memberNames[userBId]);
                auxB.getFriends().add(memberNames[userAId]);
            }
        }
        return new InputParams(network,users[0],users[1]);
    }
    static List<String> getChainOfFriends(InputParams inputs){
        UserQueue queueA = new UserQueue(inputs.people.get(inputs.userA));
        UserQueue queueB = new UserQueue(inputs.people.get(inputs.userB));

        while (queueA.hasNext() && queueB.hasNext())
        {
            User joint = discoverPath(inputs.people, queueA,queueB);
            if (joint != null)
                return combinePath(queueA, queueB, joint);
            joint = discoverPath(inputs.people, queueB,queueA);
            if (joint != null)
                return combinePath(queueA, queueB, joint);

        }
        return new ArrayList<>();
    }

    static User discoverPath(HashMap<String,User> network, UserQueue queueA, UserQueue queueB)
    {
        int count = queueA.queue.size();
        for (int i = 0;i<count;i++)
        {
            User person = queueA.queue.poll();
            if (queueB.isVisited.containsKey(person.getName()))
                 return person;
            for (String userName : person.getFriends())
                if (!queueA.isVisited.containsKey(userName))
            {
                queueA.queue.add(network.get(userName));
                queueA.isVisited.put(userName,person);
            }
        }
        return null;
    }

    static List<String> combinePath(UserQueue queueA, UserQueue queueB, User joint)
    {
        List<String> list = new LinkedList<String>();
        list.add(joint.getName());
        User aux = queueA.isVisited.get(joint.getName());
        while (aux!=null)
        {
            list.add(0,aux.getName());
            aux = queueA.isVisited.get(aux.getName());
        }
        aux = queueB.isVisited.get(joint.getName());
        while (aux!=null)
        {
            list.add(aux.getName());
            aux = queueB.isVisited.get(aux.getName());
        }
        return list;
    }
}


