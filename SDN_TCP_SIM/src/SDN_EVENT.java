
public class SDN_EVENT {
	
	public String EVENT_TYPE = "";
	public double EVENT_TIME;
	
	public int FLOW_NUMBER;
	

	public static String DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT = "DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT";
	public static String SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT = "SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT";
	public static String SYN_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT = "SYN_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT";
	public static String SYN_ARRIVAL_AT_CONTROLLER_EVENT = "SYN_ARRIVAL_AT_CONTROLLER_EVENT";
	public static String SWITCH_SERVICE_EVENT = "SWITCH_SERVICE_EVENT";
	public static String TCP_RELINQUISH_EVENT = "TCP_RELINQUISH_EVENT";
	public static String CONTROLLER_SERVICE_EVENT = "CONTROLLER_SERVICE_EVENT";
	
	public SDN_EVENT(){}
	
	
	public SDN_EVENT(String event_type, double event_time){
		this.EVENT_TYPE = event_type;
		this.EVENT_TIME = event_time;
		this.FLOW_NUMBER = 0;
	}
	
	public SDN_EVENT(String event_type, double event_time,int flow_id){
		this.EVENT_TYPE = event_type;
		this.EVENT_TIME = event_time;
		this.FLOW_NUMBER = flow_id;
	}
	
	public void set_Event_Type(String event_type){
		this.EVENT_TYPE = event_type;
	}
	
	public String get_Event_Type(){
		return this.EVENT_TYPE;
	}
	
	public void set_Event_Time(double event_time){
		this.EVENT_TIME = event_time;
	}
	
	public double get_Event_Time(){
		return this.EVENT_TIME;
	}
	
	public int getFLOW_NUMBER() {
		return FLOW_NUMBER;
	}

	public void setFLOW_NUMBER(int flow_no) {
		this.FLOW_NUMBER = flow_no;
	}
	
	
}
