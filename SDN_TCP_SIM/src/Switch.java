
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Switch {
    
     Queue<Packet> switchqueue;
    int Ntcp1count;
    int Ntcp2count;
    
   
    public void init(){
    	
        this.Ntcp1count=0;
        this.Ntcp2count=0;
        switchqueue = new LinkedList<Packet>();
    }
        
    
}
