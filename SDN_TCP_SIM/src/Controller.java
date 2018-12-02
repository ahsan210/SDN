
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Controller {
    
    Queue<Packet> controllerQueue;
    
    public void  init(){
        this.controllerQueue = new LinkedList<Packet>();
    }
    
}
