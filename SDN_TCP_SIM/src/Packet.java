
public class Packet {
	
    String packet_type="";
    int flow_no;
    
    private double arrival_time_switch;
	private double arrival_time_controller;
    private double arrival_second_time_switch;
    
    private double departure_time_switch;
    private double departure_time_controller;
    private double departure_second_time_switch;

    private double wait_time_switch;
    private double wait_time_controller;
    private double wait_second_time_switch;
    
    boolean ControllerVisited;

    
    public String getPacket_type() {
		return this.packet_type;
	}

	public void setPacket_type(String packet_type) {
		this.packet_type = packet_type;
	}

	public Packet(String type, double arrival_time,int flow_id){
        
    	this.packet_type = type;
        this.arrival_time_switch = arrival_time;
        this.ControllerVisited = false;
        this.flow_no = flow_id;
    }

    public Packet(String type, double arrival_time,boolean visited,int flow_id){
        
    	this.packet_type = type;
    	this.arrival_time_switch = arrival_time;
    	this.ControllerVisited = visited;
    	this.flow_no = flow_id;
    }
        
    public void set_arrival_time_controller(double time){
    	this.arrival_time_controller = time;
    }
    
    public void set_arrival_second_time_switch(double time){
    	this.arrival_second_time_switch = time;
    }
    
    public void set_departure_time_switch(double time){
    	this.departure_time_switch = time;
    }
    
    public void set_departure_time_controller(double time){
    	this.departure_time_controller = time;
    }
    
    public void set_departure_second_time_switch(double time){
    	this.departure_second_time_switch = time;
    }
    
    public void set_wait_time_switch(){
    	this.wait_time_switch = this.departure_time_switch - this.arrival_time_switch;
    }
    
    public void set_wait_time_controller(){
    	this.wait_time_controller = this.departure_time_controller - this.arrival_time_controller;
    }
    
    public void set_wait_second_time_switch(){
    	this.wait_second_time_switch = this.departure_second_time_switch - this.arrival_second_time_switch;
    }
    
    public double Total_data_delay(){
    	
    	set_wait_time_switch();
    	return wait_time_switch;
    }
    
    public double Total_SYN_delay(){
    	
    	set_wait_time_switch();
    	set_wait_time_controller();
    	set_wait_second_time_switch();
    	double syn_delay = wait_time_switch + wait_time_controller + wait_second_time_switch + 2.0* (1.0/SDNTCP.DSC);
    	return syn_delay;
    }
    
    public void set_controller_visited(boolean visited){
    	
    	this.ControllerVisited = visited;
    }
    
    public double get_arrival_time_switch(){
    	return this.arrival_time_switch;
    }
    
    public double get_arrival_time_controller(){
    	return this.arrival_time_controller;
    }
    
    public double get_arrival_second_time_switch(){
    	return this.arrival_second_time_switch;
    }
    
    public double get_departure_time_switch(){
    	return this.departure_time_switch;
    }
    
    public double get_departure_time_controller(){
    	return this.departure_time_controller;
    }
    
    public double get_departure_second_time_switch(){
    	return this.departure_second_time_switch;
    }
    
    public int getFlow_no() {
		return flow_no;
	}

	public void setFlow_no(int flow_no) {
		this.flow_no = flow_no;
	}
    
}
