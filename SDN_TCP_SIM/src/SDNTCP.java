
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SDNTCP {
	
	static String PATH = "Result"; 
	//Random Variables
	static Random Event_random = new Random();
	
	//Baseline Parameter Values
	static double Shape_value = 0.95;//Weibull distribution parameter
    static double Lembda_P = 250;
    static double Lembda_TCP = 150;
    static double Switch_MEU = 50000;
    static double Controller_MEU = 1000;
    static double Switch_MEU_TCP = 0.8;
    static double DSC = 100000;
    static int Max_SwitchLength = 100;
    static int Max_ControllerLength = 100;
    
    //Simulation Variables
    static long Maximum_data_packet = 0;
    static double Simulation_time  = 3;
    static int Target_runcount = 10000;
    static double Parameter_Value = 0;
    static String Parameter_Name = "";
    static int Max_queue_length = 0;
    static int Avg_max_queue_length = 0;
    
    //Simulation Flags
    static boolean First_SYN_packet_arrived_switch = false;
    static boolean First_SYN_packet_arrived_controller = false;
    
    
    //Per Iteration data
    static long Total_switch_NTCP2 = 0;
	static long Total_switch_incoming = 0;
    static long Total_controller_incoming = 0;
    static long Total_switch_incoming_data_packet = 0;
    static long Total_switch_incoming_SYN_packet = 0;
    static long Total_switch_incoming_data_packet_source = 0;
    static long Total_switch_incoming_SYN_packet_source = 0;
    static long Total_switch_incoming_SYN_packet_controller = 0;
    static long Total_controller_incoming_SYN_packet = 0;
    static long Total_served_data_packet = 0;
    static long Total_served_SYN_packet = 0;
    static long Total_served_switch = 0;
    static long Total_served_controller = 0;
    static long Total_served_SYN_packet_switch_first = 0;
    static long Total_served_SYN_packet_switch_second = 0;
    static long Total_served_SYN_packet_controller = 0;
    static long Total_data_packet_dropped = 0;
    static long Total_SYN_packet_dropped = 0;
    static long Total_switch_packet_dropped = 0;
    static long Total_controller_packet_dropped = 0;
    static long Total_SYN_packet_dropped_switch_first = 0;
    static long Total_SYN_packet_dropped_switch_second = 0;
    static long Total_SYN_packet_dropped_controller = 0;
    static long Total_TCP_termination = 0;
    static long Total_switch_queue_size = 0;
    static long Total_controller_queue_size = 0;
    static long Total_event_count = 0;
        
    static double Total_data_packet_delay = 0;
    static double Total_SYN_packet_delay = 0;
    

    //Overall Iteration data
    static long Avg_switch_NTCP2 = 0;
    static long Avg_switch_incoming = 0;
    static long Avg_controller_incoming = 0;
    static long Avg_switch_incoming_data_packet = 0;
    static long Avg_switch_incoming_SYN_packet = 0;
    static long Avg_switch_incoming_data_packet_source = 0;
    static long Avg_switch_incoming_SYN_packet_source = 0;
    static long Avg_switch_incoming_SYN_packet_controller = 0;
    static long Avg_controller_incoming_SYN_packet = 0;
    static long Avg_served_data_packet = 0;
    static long Avg_served_SYN_packet = 0;
    static long Avg_served_switch = 0;
    static long Avg_served_controller = 0;
    static long Avg_served_SYN_packet_switch_first = 0;
    static long Avg_served_SYN_packet_switch_second = 0;
    static long Avg_served_SYN_packet_controller = 0;
    static long Avg_data_packet_dropped = 0;
    static long Avg_SYN_packet_dropped = 0;
    static long Avg_switch_packet_dropped = 0;
    static long Avg_controller_packet_dropped = 0;
    static long Avg_SYN_packet_dropped_switch_first = 0;
    static long Avg_SYN_packet_dropped_switch_second = 0;
    static long Avg_SYN_packet_dropped_controller = 0;
    static long Avg_TCP_termination = 0;
    static long Avg_switch_queue_size = 0;
    static long Avg_controller_queue_size = 0;
    
    static double Mean_data_Delay;
    static double Mean_SYN_Delay;
    static double Mean_data_Drop;
    static double Mean_SYN_Drop;
    static double Mean_switch_drop;
    static double Mean_controller_drop;

    //Data Structure 
    static Switch SDN_switch;
    static Controller SDN_controller;
    static Queue<SDN_EVENT> SDN_EVENT_PriorityQueue;
    static Queue<Packet>Switch_to_Controller_Line;
    static Queue<Packet>Controller_to_Switch_Line;
    static Queue<Integer>TCP2_FLOWS_ACTIVE;
    
    static int TCP_FLOW_NUMBER = 0;
    static String DATA_PACKET = "DATA";
    static String SYN_PACKET = "SYN";

    
   

    //Simulation Program Iteration
    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 21; i++) {

//        	Lembda_P = 200;             	
//          Lembda_P = Lembda_P + i * 5;
            
            Lembda_TCP = 100;
            Lembda_TCP = Lembda_TCP + i * 5;
        	
        		
//        	Switch_MEU_TCP = 1.5;            	
//        	Switch_MEU_TCP = Switch_MEU_TCP - i*0.05;
        	
//        	Switch_MEU = 55000 - i*500;
            	
//        	Controller_MEU = 800 + i*25;
        	
            Parameter_Value = Lembda_TCP;
            Parameter_Name = "Lembda_TCP";
            
            Maximum_data_packet = (long)(Lembda_TCP*Lembda_P*Simulation_time);
            
            Intialize_All();
            
            for (int j = 1; j <= Target_runcount; j++) {
            		
            	Initialize();

                Simulation_Main();
                
                Calculation(j);
                
            }
            
            System.out.println(Parameter_Value + " iteration complete\n\n\n");
            
            try {
                FinalResult();

            } catch (IOException ex) {
                Logger.getLogger(SDNTCP.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        System.out.println("\n--------------Simulation Complete-----------------");

    }

    //Variable initialization before each iteration
    public static void Initialize() {
    	
    	Event_random.setSeed(System.nanoTime()); 
    	

    	SDN_EVENT_PriorityQueue = new PriorityQueue<>(10, SDN_EVENT_Comparator);
    	SDN_switch = new Switch();
    	SDN_controller = new Controller();
        SDN_switch.init();
        SDN_controller.init();
        Switch_to_Controller_Line = new LinkedList<Packet>();
        Controller_to_Switch_Line = new LinkedList<Packet>();
        TCP2_FLOWS_ACTIVE = new LinkedList<Integer>();
        
        Max_queue_length = 0;
        Total_switch_NTCP2 = 0;
        Total_switch_incoming = 0;
        Total_controller_incoming = 0;
        Total_switch_incoming_data_packet = 0;
        Total_switch_incoming_SYN_packet = 0;
        Total_switch_incoming_data_packet_source = 0;
        Total_switch_incoming_SYN_packet_source = 0;
        Total_switch_incoming_SYN_packet_controller = 0;
        Total_controller_incoming_SYN_packet = 0;
        Total_served_data_packet = 0;
        Total_served_SYN_packet = 0;
        Total_served_switch = 0;
        Total_served_controller = 0;
        Total_served_SYN_packet_switch_first = 0;
        Total_served_SYN_packet_switch_second = 0;
        Total_served_SYN_packet_controller = 0;
        Total_data_packet_dropped = 0;
        Total_SYN_packet_dropped = 0;
        Total_switch_packet_dropped = 0;
        Total_controller_packet_dropped = 0;
        Total_SYN_packet_dropped_switch_first = 0;
        Total_SYN_packet_dropped_switch_second = 0;
        Total_SYN_packet_dropped_controller = 0;
        Total_TCP_termination = 0;
        Total_switch_queue_size = 0;
        Total_controller_queue_size = 0;
        Total_event_count = 0;
        
        Total_data_packet_delay = 0;
        Total_SYN_packet_delay = 0;
        
        TCP_FLOW_NUMBER = 0;
        TCP_FLOW_NUMBER++;
        
        First_SYN_packet_arrived_switch = false;
    	First_SYN_packet_arrived_controller = false;
    }

    //Variable initialization before each new parameter setting
    public static void Intialize_All() {
    	
        Avg_max_queue_length = 0;
    	Avg_switch_NTCP2 = 0;
    	Avg_switch_incoming = 0;
        Avg_controller_incoming = 0;
        Avg_switch_incoming_data_packet = 0;
        Avg_switch_incoming_SYN_packet = 0;
        Avg_switch_incoming_data_packet_source = 0;
        Avg_switch_incoming_SYN_packet_source = 0;
        Avg_switch_incoming_SYN_packet_controller = 0;
        Avg_controller_incoming_SYN_packet = 0;
        Avg_served_data_packet = 0;
        Avg_served_SYN_packet = 0;
        Avg_served_switch = 0;
        Avg_served_controller = 0;
        Avg_served_SYN_packet_switch_first = 0;
        Avg_served_SYN_packet_switch_second = 0;
        Avg_served_SYN_packet_controller = 0;
        Avg_data_packet_dropped = 0;
        Avg_SYN_packet_dropped = 0;
        Avg_switch_packet_dropped = 0;
        Avg_controller_packet_dropped = 0;
        Avg_SYN_packet_dropped_switch_first = 0;
        Avg_SYN_packet_dropped_switch_second = 0;
        Avg_SYN_packet_dropped_controller = 0;
        Avg_TCP_termination = 0;
        Avg_switch_queue_size = 0;
        Avg_controller_queue_size = 0;
    	
        Mean_data_Delay = 0;
        Mean_SYN_Delay = 0;
        Mean_data_Drop = 0;
        Mean_SYN_Drop = 0;
        Mean_switch_drop = 0;
        Mean_controller_drop = 0;
    }

    static double Poisson_expon(double mean,String expon_type) {
    	
    	double rand = 0;
    	rand = Event_random.nextDouble();
    	
//Use in case of using Weibull distribution for Lambda_TCP Parameter    	
//    	if(expon_type.equals(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)){
//    		
//    		return Math.pow((-Math.log(1.0-rand)), 1.0/Shape_value)/mean;
//    	}
//    	else{
//    		return (-Math.log(1.0-rand))/mean;	
//    	}

//Use in case of Poison or Exponential distribution for All Parameter    	
		return (-Math.log(1.0-rand))/mean;			
    }
    
	public static Comparator<SDN_EVENT> SDN_EVENT_Comparator = new Comparator<SDN_EVENT>(){
			
		@Override
		public int compare(SDN_EVENT e1, SDN_EVENT e2) {
			
			return Double.compare(e1.get_Event_Time(),e2.get_Event_Time());
        }
	};

    public static void Calculation(int iter) throws IOException{

    	Total_switch_incoming_data_packet = Total_switch_incoming_data_packet_source;
    	Total_switch_incoming_SYN_packet = Total_switch_incoming_SYN_packet_source;
    	Total_switch_queue_size = Total_switch_queue_size/Total_event_count;
    	Total_controller_queue_size = Total_controller_queue_size/Total_event_count;
    	Total_switch_NTCP2 = Total_switch_NTCP2 / Total_event_count;
    	
    	Avg_max_queue_length+=Max_queue_length;
    	Avg_switch_NTCP2 += Total_switch_NTCP2;
    	Avg_switch_incoming += Total_switch_incoming;
        Avg_controller_incoming += Total_controller_incoming;
        Avg_switch_incoming_data_packet += Total_switch_incoming_data_packet;
        Avg_switch_incoming_SYN_packet += Total_switch_incoming_SYN_packet;
        Avg_switch_incoming_data_packet_source += Total_switch_incoming_data_packet_source;
        Avg_switch_incoming_SYN_packet_source += Total_switch_incoming_SYN_packet_source;
        Avg_switch_incoming_SYN_packet_controller += Total_switch_incoming_SYN_packet_controller;
		Avg_controller_incoming_SYN_packet += Total_controller_incoming_SYN_packet;
		
		Avg_served_data_packet += Total_served_data_packet;
		Avg_served_SYN_packet += Total_served_SYN_packet;
		Avg_served_switch += Total_served_switch;
		Avg_served_controller += Total_served_controller;
		Avg_served_SYN_packet_switch_first += Total_served_SYN_packet_switch_first;
		Avg_served_SYN_packet_switch_second += Total_served_SYN_packet_switch_second;
		Avg_served_SYN_packet_controller += Total_served_SYN_packet_controller;
		Avg_data_packet_dropped += Total_data_packet_dropped;
		Avg_SYN_packet_dropped += Total_SYN_packet_dropped;
		Avg_switch_packet_dropped += Total_switch_packet_dropped;
	    Avg_controller_packet_dropped += Total_controller_packet_dropped;
		Avg_SYN_packet_dropped_switch_first += Total_SYN_packet_dropped_switch_first;
		Avg_SYN_packet_dropped_switch_second += Total_SYN_packet_dropped_switch_second;
		Avg_SYN_packet_dropped_controller += Total_SYN_packet_dropped_controller;
		Avg_TCP_termination += Total_TCP_termination;
		Avg_switch_queue_size += Total_switch_queue_size;
		Avg_controller_queue_size += Total_controller_queue_size;
		
		Mean_data_Delay += Total_data_packet_delay/Total_served_data_packet;
        Mean_SYN_Delay += Total_SYN_packet_delay/Total_served_SYN_packet;
        Mean_data_Drop += (double)((double)Total_data_packet_dropped/(Total_switch_incoming_data_packet));
        Mean_SYN_Drop += (double)((double)Total_SYN_packet_dropped/(Total_switch_incoming_SYN_packet));
        Mean_switch_drop += (double)((double)Total_switch_packet_dropped/(Total_switch_incoming_data_packet+Total_switch_incoming_SYN_packet));
        Mean_controller_drop += (double)((double)Total_controller_packet_dropped/(Total_controller_incoming));
    	
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_Data_Delay_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Total_data_packet_delay/Total_served_data_packet);
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_SYN_Delay_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Total_SYN_packet_delay/Total_served_SYN_packet);
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_Data_Drop_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + (double)((double)Total_data_packet_dropped/(Total_switch_incoming_data_packet)));
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_SYN_Drop_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + (double)((double)Total_SYN_packet_dropped/(Total_switch_incoming_SYN_packet)));
            
        }
    	
    	SDN_EVENT_PriorityQueue.clear();
    	Switch_to_Controller_Line.clear();
    	Controller_to_Switch_Line.clear();
    	TCP2_FLOWS_ACTIVE.clear();
    }

    public static void FinalResult() throws IOException {
        	
        try (FileWriter fw = new FileWriter(PATH+"Data_Delay_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_data_Delay / Target_runcount);
            
        }
        
        try (FileWriter fw = new FileWriter(PATH+"Data_Drop_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_data_Drop / Target_runcount);
        } 
        
        try (FileWriter fw = new FileWriter(PATH+"SYN_Delay_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_SYN_Delay / Target_runcount);
        } 
        
        try (FileWriter fw = new FileWriter(PATH+"SYN_Drop_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_SYN_Drop / Target_runcount);
        }
        
        try (FileWriter fw = new FileWriter(PATH+"Switch_Controller_Drop_TCP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_switch_drop / Target_runcount+ "    "+Mean_controller_drop/ Target_runcount);
        }
        
        try (FileWriter fw = new FileWriter(PATH+"TCP_DATA_STATISTICS.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
        	
        	out.println("Parameters : \n");
        	out.println("Lembda_P : "+Lembda_P);
        	out.println("Lembda_TCP : "+Lembda_TCP);
        	out.println("Switch_MEU : "+Switch_MEU);
        	out.println("Controller_MEU : "+Controller_MEU);
        	out.println("Switch_MEU_TCP : "+Switch_MEU_TCP);
        	out.println("DSC : "+DSC);
        	out.println("Max_SwitchLength : "+Max_SwitchLength);
        	out.println("Max_ControllerLength : "+Max_ControllerLength);
        	out.println("Maximum_data_packet : "+Maximum_data_packet);
        	out.println("Simulation_time : "+Simulation_time);
        	out.println("Target_runcount : "+Target_runcount);
        	out.println("---------------------------------------------");
        	out.println("\n\nParameter_Name : "+Parameter_Name);
            out.println(Parameter_Name+" : "+Parameter_Value);
            out.println("\nPacket Incoming :\nAvg_switch_incoming : "+Avg_switch_incoming/ Target_runcount);
            out.println("Avg_controller_incoming : "+Avg_controller_incoming/ Target_runcount);
            out.println("Avg_switch_incoming_data_packet : "+Avg_switch_incoming_data_packet/ Target_runcount);
            out.println("Avg_switch_incoming_SYN_packet : "+Avg_switch_incoming_SYN_packet/ Target_runcount);
            out.println("Avg_switch_incoming_data_packet_source : "+Avg_switch_incoming_data_packet_source/ Target_runcount);
            out.println("Avg_switch_incoming_SYN_packet_source : "+Avg_switch_incoming_SYN_packet_source/ Target_runcount);
            out.println("Avg_switch_incoming_SYN_packet_controller : "+Avg_switch_incoming_SYN_packet_controller/ Target_runcount);
            out.println("Avg_controller_incoming_SYN_packet : "+Avg_controller_incoming_SYN_packet/ Target_runcount);
            out.println("\nPacket Service :\nAvg_served_data_packet : "+Avg_served_data_packet/ Target_runcount);
            out.println("Avg_served_SYN_packet : "+Avg_served_SYN_packet/ Target_runcount);
            out.println("Avg_served_switch : "+Avg_served_switch/ Target_runcount);
            out.println("Avg_served_controller : "+Avg_served_controller/ Target_runcount);
            out.println("Avg_served_SYN_packet_switch_first : "+Avg_served_SYN_packet_switch_first/ Target_runcount);
            out.println("Avg_served_SYN_packet_switch_second : "+Avg_served_SYN_packet_switch_second/ Target_runcount);
            out.println("Avg_served_SYN_packet_controller : "+Avg_served_SYN_packet_controller/ Target_runcount);
            out.println("\nPacket Drop :\nAvg_data_packet_dropped : "+Avg_data_packet_dropped/ Target_runcount);
            out.println("Avg_SYN_packet_dropped : "+Avg_SYN_packet_dropped/ Target_runcount);
            out.println("Avg_switch_packet_dropped : "+Avg_switch_packet_dropped/ Target_runcount);
            out.println("Avg_controller_packet_dropped : "+Avg_controller_packet_dropped/ Target_runcount);
            out.println("Avg_SYN_packet_dropped_switch_first : "+Avg_SYN_packet_dropped_switch_first/ Target_runcount);
            out.println("Avg_SYN_packet_dropped_switch_second : "+Avg_SYN_packet_dropped_switch_second/ Target_runcount);
            out.println("Avg_SYN_packet_dropped_controller : "+Avg_SYN_packet_dropped_controller/ Target_runcount);
            out.println("\nOthers : \nAvg_switch_queue_size : "+Avg_switch_queue_size/ Target_runcount);
            out.println("Avg_controller_queue_size : "+Avg_controller_queue_size/ Target_runcount);
            out.println("Avg_TCP_termination : "+Avg_TCP_termination/ Target_runcount); 
            out.println("Avg_switch_NTCP2 : "+Avg_switch_NTCP2/ Target_runcount);
            out.println("\n\n----------------------------#####################---******---#####################----------------------------\n\n");
        }
        
        System.out.println("Max queue length : "+Avg_max_queue_length/Target_runcount);
    } 
     
    //Data Packet arrival event 
    private static void Handle_Data_Arrival(SDN_EVENT sdn_event) {    	
    	        
    	double simu_time = sdn_event.get_Event_Time();		
    	int flow_no = sdn_event.getFLOW_NUMBER();
    	
        Packet packet = new Packet(DATA_PACKET, simu_time,flow_no);

        SDN_switch.switchqueue.add(packet);
        	
       	double next_data_arrival = simu_time + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival,flow_no));
             
     }

    //SYN Packet arrival at Switch from Source event handle
    private static void Handle_SYN_Arrival_Switch_from_Source(SDN_EVENT sdn_event){
    	
    	double simu_time = sdn_event.get_Event_Time(); 
    	int flow_no = sdn_event.getFLOW_NUMBER();
        
        SDN_switch.Ntcp1count++;
        
        Packet packet = new Packet(SYN_PACKET, simu_time, false,flow_no);
        SDN_switch.switchqueue.add(packet);       

        TCP_FLOW_NUMBER++;
        double next_syn_arrival_time = simu_time + Poisson_expon(Lembda_TCP,SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
    	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_syn_arrival_time,TCP_FLOW_NUMBER));
    		
    	if(!First_SYN_packet_arrived_switch){
        	
        	double next_switch_departure = simu_time + Poisson_expon(Switch_MEU,SDN_EVENT.SWITCH_SERVICE_EVENT);
            SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SWITCH_SERVICE_EVENT, next_switch_departure));
            
            First_SYN_packet_arrived_switch = true;
        }            
    }
    
  //SYN Packet arrival at Switch from Controller event handle
    private static void Handle_SYN_Arrival_Switch_from_Controller(SDN_EVENT sdn_event) {
    	
    	double simu_time = sdn_event.get_Event_Time();

    	Packet packet = Controller_to_Switch_Line.remove();
    	
    	SDN_switch.Ntcp1count--;
    	SDN_switch.Ntcp2count++;
    	
    	TCP2_FLOWS_ACTIVE.add(packet.getFlow_no());
        
        double next_data_arrival_time = simu_time + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival_time,packet.getFlow_no()));

        double next_tcp_terminate  = simu_time + Poisson_expon(Switch_MEU_TCP,SDN_EVENT.TCP_RELINQUISH_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.TCP_RELINQUISH_EVENT, next_tcp_terminate,packet.getFlow_no()));

        packet.set_controller_visited(true);
        packet.set_arrival_second_time_switch(simu_time);
        
        SDN_switch.switchqueue.add(packet);  
    	
    }
    
    //Switch service event handle
    private static void Handle_Switch_Departure(SDN_EVENT sdn_event) {
    	    	    	
    	double simu_time = sdn_event.get_Event_Time();
    	
        Packet packet = SDN_switch.switchqueue.remove();

        Total_served_switch++;
        
        if (packet.packet_type.equals(DATA_PACKET)) {
	
    		packet.set_departure_time_switch(simu_time);
    		Total_data_packet_delay += packet.Total_data_delay();
    		
            Total_served_data_packet++;
                    
        }   
        else{
        	
    		if(packet.ControllerVisited) {
            	
                packet.set_departure_second_time_switch(simu_time);
                Total_SYN_packet_delay += packet.Total_SYN_delay();
                 
                Total_served_SYN_packet++;
                Total_served_SYN_packet_switch_second++;
               
             }else { 
                 
                 packet.set_departure_time_switch(simu_time);
                 Switch_to_Controller_Line.add(packet);
                                
                 Total_served_SYN_packet_switch_first++;
                 
                 sdn_event.set_Event_Time(simu_time+ (1.0/DSC));
                 sdn_event.set_Event_Type(SDN_EVENT.SYN_ARRIVAL_AT_CONTROLLER_EVENT);
                 SDN_EVENT_PriorityQueue.add(sdn_event);

             }        	
        }

    	double next_switch_departure = simu_time + Poisson_expon(Switch_MEU,SDN_EVENT.SWITCH_SERVICE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SWITCH_SERVICE_EVENT, next_switch_departure));
        
    }

    //TCP flow terminate event handle
    private static void Handle_TCP_departure(SDN_EVENT sdn_event) {
    	      
    	Total_TCP_termination++;
		SDN_switch.Ntcp2count--;
		
		TCP2_FLOWS_ACTIVE.remove(new Integer(sdn_event.getFLOW_NUMBER()));
	}
   
    //SYN Packet arrival at Controller from Switch event handle
    private static void Handle_Controller_Arrival(SDN_EVENT sdn_event) { 
    	 	 
    	double simu_time = sdn_event.get_Event_Time();
    	
    	Packet packet = Switch_to_Controller_Line.remove();
    	
    	packet.set_arrival_time_controller(simu_time);
    	
    	SDN_controller.controllerQueue.add(packet);
    	
    	if(!First_SYN_packet_arrived_controller){
    		
    		double next_controller_departure = simu_time + Poisson_expon(Controller_MEU,SDN_EVENT.CONTROLLER_SERVICE_EVENT);
    		SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.CONTROLLER_SERVICE_EVENT, next_controller_departure));
            
            First_SYN_packet_arrived_controller = true;
    	}
    	
    }
    
    //Controller service event handle
    private static void Handle_Controller_Departure(SDN_EVENT sdn_event) {
    	
        double simu_time = sdn_event.get_Event_Time();
        
        Packet packet = SDN_controller.controllerQueue.remove();
        
        packet.set_departure_time_controller(simu_time);
        
        Controller_to_Switch_Line.add(packet);
       	
       	sdn_event.set_Event_Time(simu_time + (1.0/DSC));
   		sdn_event.set_Event_Type(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT);
   		SDN_EVENT_PriorityQueue.add(sdn_event);
   		
   		Total_served_controller++;
   		Total_served_SYN_packet_controller++;
       	       	
        double next_controller_departure = simu_time + Poisson_expon(Controller_MEU,SDN_EVENT.CONTROLLER_SERVICE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.CONTROLLER_SERVICE_EVENT, next_controller_departure));
    }
    
    //Main Simulation Program
    private static void Simulation_Main() {
    	
    	double syn_arrival_time = Poisson_expon(Lembda_TCP,SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
    	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, syn_arrival_time,TCP_FLOW_NUMBER)); 
    	
    	Max_queue_length = -1;
        while (Total_switch_incoming_data_packet_source < Maximum_data_packet) {

        	if(Max_queue_length<SDN_switch.switchqueue.size()){
        		Max_queue_length = SDN_switch.switchqueue.size();
        	}
        	
          Total_switch_queue_size += SDN_switch.switchqueue.size();
          Total_controller_queue_size +=SDN_controller.controllerQueue.size();
          Total_switch_NTCP2 += SDN_switch.Ntcp2count;
          
          Total_event_count++;
          
          SDN_EVENT sdn_event = (SDN_EVENT)SDN_EVENT_PriorityQueue.poll();
          
          if(sdn_event.get_Event_Type().equals(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)) {

        	  if(TCP2_FLOWS_ACTIVE.contains(new Integer(sdn_event.getFLOW_NUMBER()))){
        		
        		  Total_switch_incoming++;
              	  Total_switch_incoming_data_packet_source++;
              	  
        		  if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
                  	
                  	Handle_Data_Arrival(sdn_event);
                  
                  } else {
                  	
                	Total_switch_packet_dropped++;
                  	Total_data_packet_dropped++;
                  	
                   	double next_data_arrival = sdn_event.get_Event_Time() + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
                   	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival,sdn_event.getFLOW_NUMBER()));
                   	
                  }
        	  }
                
            }else if (sdn_event.get_Event_Type().equals(SDN_EVENT.SWITCH_SERVICE_EVENT)) {

                if (SDN_switch.switchqueue.size() > 0) {
                    
                	Handle_Switch_Departure(sdn_event);

                }else{
                	
                	double next_switch_departure = sdn_event.get_Event_Time() + Poisson_expon(Switch_MEU,SDN_EVENT.SWITCH_SERVICE_EVENT);
                    SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SWITCH_SERVICE_EVENT, next_switch_departure));
                }

            }else if(sdn_event.get_Event_Type().equals(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)){

            	Total_switch_incoming++;
            	Total_switch_incoming_SYN_packet_source++;

                if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
                	
                	Handle_SYN_Arrival_Switch_from_Source(sdn_event);

                } else {
                	
                	Total_switch_packet_dropped++;
                	Total_SYN_packet_dropped_switch_first++;
                	Total_SYN_packet_dropped++;
                	TCP_FLOW_NUMBER++;
                	
                	double next_syn_arrival_time = sdn_event.get_Event_Time() + Poisson_expon(Lembda_TCP,SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
                	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_syn_arrival_time,TCP_FLOW_NUMBER));

                }

            }
            else if (sdn_event.get_Event_Type().equals(SDN_EVENT.SYN_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT)){
            	
           		Total_switch_incoming++;
           		Total_switch_incoming_SYN_packet_controller++;
           		
               	if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
                	 		
               		Handle_SYN_Arrival_Switch_from_Controller(sdn_event);

                } else {
                	
                	SDN_switch.Ntcp1count--;
                	Total_switch_packet_dropped++;
                	Total_SYN_packet_dropped++;
                	Total_SYN_packet_dropped_switch_second++;
                	
                	Controller_to_Switch_Line.remove();
                	
                }
            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.TCP_RELINQUISH_EVENT)){
            	
            	if (TCP2_FLOWS_ACTIVE.size() > 0) {
                	
                    Handle_TCP_departure(sdn_event);  
                } 
            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.SYN_ARRIVAL_AT_CONTROLLER_EVENT)){
            	
            	 Total_controller_incoming++;
                 Total_controller_incoming_SYN_packet++;

                 if (SDN_controller.controllerQueue.size() < Max_ControllerLength) {
                	 
                 	Handle_Controller_Arrival(sdn_event);

                 } else {
                 	
                	SDN_switch.Ntcp1count--;
                	Total_SYN_packet_dropped++;
                 	Total_SYN_packet_dropped_controller++;
                 	Total_controller_packet_dropped++;
                 	
                 	Switch_to_Controller_Line.remove();
                 	
                 }
            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.CONTROLLER_SERVICE_EVENT)){

                if(SDN_controller.controllerQueue.size()>0) {
                	
                	Handle_Controller_Departure(sdn_event);
                
                }else{
                	
                	double next_controller_departure = sdn_event.get_Event_Time() + Poisson_expon(Controller_MEU,SDN_EVENT.CONTROLLER_SERVICE_EVENT);
                    SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.CONTROLLER_SERVICE_EVENT, next_controller_departure));
                }
            }
          
        } 
       
    }

}

