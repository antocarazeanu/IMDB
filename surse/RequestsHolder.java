import java.util.ArrayList;
public class RequestsHolder {
    private static ArrayList<Request> requests = new ArrayList<Request>();
    // methods
    public static void addRequest(Request request){
        requests.add(request);
    }
    public static void deleteRequest(Request request){
        requests.remove(request);
    }
    // getters
    public static ArrayList<Request> getRequests(){
        return requests;
    }

    // setters
    public static void setRequests(ArrayList<Request> requests) {
        RequestsHolder.requests = requests;
    }
}
