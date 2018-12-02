
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Switch {
    
     Queue<Packet> switchqueue;
    int Nudp1count;
    int Nudp2count;
    
   
    public void init(){
    	
        this.Nudp1count=0;
        this.Nudp2count=0;
        switchqueue = new LinkedList<Packet>();
    }
        
    
}
