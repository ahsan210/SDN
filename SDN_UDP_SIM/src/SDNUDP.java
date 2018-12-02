
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


public class SDNUDP {
	
	static String PATH = "Result"; 
	//Random Variables
	static Random Event_random = new Random();
	
	//Baseline Parameter Values
	static double Shape_value = 0.95;//Weibull distribution parameter
    static double Lembda_P = 250;
    static double Lembda_UDP = 150;    
    static double Switch_MEU = 50000;
    static double Controller_MEU = 1000;
    static double Switch_MEU_UDP = 0.8;
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
    static int avg_max_queue_length = 0;
    static int UDP_data_fault = 1;
    
    //Simulation Flags
    static boolean First_UDP_packet_arrived_switch = false;
    static boolean First_UDP_packet_arrived_controller = false;
    
    
    //Per Iteration data
    static long Total_switch_NUDP2 = 0;
    static long Total_switch_incoming = 0;
    static long Total_controller_incoming = 0;
    static long Total_switch_incoming_data_packet = 0;
    static long Total_switch_incoming_UDP_packet = 0;
    static long Total_switch_incoming_data_packet_source = 0;
    static long Total_switch_incoming_UDP_packet_source = 0;
    static long Total_switch_incoming_data_packet_controller = 0;
    static long Total_switch_incoming_UDP_packet_controller = 0;
    static long Total_controller_incoming_data_packet = 0;
    static long Total_controller_incoming_UDP_packet = 0;
    static long Total_served_data_packet = 0;
    static long Total_served_UDP_packet = 0;
    static long Total_served_switch = 0;
    static long Total_served_controller = 0;
    static long Total_served_data_packet_switch_first = 0;
    static long Total_served_UDP_packet_switch_first = 0;
    static long Total_served_data_packet_switch_second = 0;
    static long Total_served_UDP_packet_switch_second = 0;
    static long Total_served_data_packet_controller = 0;
    static long Total_served_UDP_packet_controller = 0;
    static long Total_data_packet_dropped = 0;
    static long Total_UDP_packet_dropped = 0;
    static long Total_switch_packet_dropped = 0;
    static long Total_controller_packet_dropped = 0;
    static long Total_data_packet_dropped_switch_first = 0;
    static long Total_UDP_packet_dropped_switch_first = 0;
    static long Total_data_packet_dropped_switch_second = 0;
    static long Total_UDP_packet_dropped_switch_second = 0;
    static long Total_data_packet_dropped_controller = 0;
    static long Total_UDP_packet_dropped_controller = 0;
    static long Total_UDP_termination = 0;
    static long Total_switch_queue_size = 0;
    static long Total_controller_queue_size = 0;
    static long Total_event_count = 0;
        
    static double Total_data_packet_delay = 0;
    static double Total_UDP_packet_delay = 0;
    

    //Overall Iteration data
    static long Avg_switch_NUDP2 = 0;
    static long Avg_switch_incoming = 0;
    static long Avg_controller_incoming = 0;
    static long Avg_switch_incoming_data_packet = 0;
    static long Avg_switch_incoming_UDP_packet = 0;
    static long Avg_switch_incoming_data_packet_source = 0;
    static long Avg_switch_incoming_UDP_packet_source = 0;
    static long Avg_switch_incoming_data_packet_controller = 0;
    static long Avg_switch_incoming_UDP_packet_controller = 0;
    static long Avg_controller_incoming_data_packet = 0;
    static long Avg_controller_incoming_UDP_packet = 0;
    static long Avg_served_data_packet = 0;
    static long Avg_served_UDP_packet = 0;
    static long Avg_served_switch = 0;
    static long Avg_served_controller = 0;
    static long Avg_served_data_packet_switch_first = 0;
    static long Avg_served_UDP_packet_switch_first = 0;
    static long Avg_served_data_packet_switch_second = 0;
    static long Avg_served_UDP_packet_switch_second = 0;
    static long Avg_served_data_packet_controller = 0;
    static long Avg_served_UDP_packet_controller = 0;
    static long Avg_data_packet_dropped = 0;
    static long Avg_UDP_packet_dropped = 0;
    static long Avg_switch_packet_dropped = 0;
    static long Avg_controller_packet_dropped = 0;
    static long Avg_data_packet_dropped_switch_first = 0;
    static long Avg_UDP_packet_dropped_switch_first = 0;
    static long Avg_data_packet_dropped_switch_second = 0;
    static long Avg_UDP_packet_dropped_switch_second = 0;
    static long Avg_data_packet_dropped_controller = 0;
    static long Avg_UDP_packet_dropped_controller = 0;
    static long Avg_UDP_termination = 0;
    static long Avg_switch_queue_size = 0;
    static long Avg_controller_queue_size = 0;
    
    static double Mean_data_Delay;
    static double Mean_UDP_Delay;
    static double Mean_data_Drop;
    static double Mean_UDP_Drop;                                                                                                                                                                                                                                                                                                                                                                                           
    static double Mean_switch_drop;
    static double Mean_controller_drop;

    //Data Structure 
    static Switch SDN_switch;
    static Controller SDN_controller;
    static Queue<SDN_EVENT> SDN_EVENT_PriorityQueue;
    static Queue<Packet>Switch_to_Controller_Line;
    static Queue<Packet>Controller_to_Switch_Line;
    static Queue<Integer>UDP1_FLOWS_ACTIVE;
    static Queue<Integer>UDP2_FLOWS_ACTIVE;
    static ArrayList<Integer> UDP_FLOW_COUNT;
    
    static int UDP_FLOW_NUMBER = 0;
    static String DATA_PACKET = "DATA";
    static String UDP_PACKET = "UDP";

    
   

    //Simulation Program Iteration
    public static void main(String[] args) throws IOException {

        for (int i = 20; i < 21; i++) {

//        	Lembda_P = 200;             	
//          Lembda_P = Lembda_P + i * 5;
            
            Lembda_UDP = 100;
            Lembda_UDP = Lembda_UDP + i * 5;
        	
        		
//        	Switch_MEU_UDP = 1.5;            	
//          Switch_MEU_UDP = Switch_MEU_UDP - i*0.05;
        	
//        	Switch_MEU = 55000 - i*500;
        	
//			Controller_MEU =  800 + i*25;
        	
        	
            Parameter_Value = Lembda_UDP;
            Parameter_Name = "Lembda_UDP"; 
            		
            Maximum_data_packet = (long)(Lembda_UDP*Lembda_P*Simulation_time);
            
            Intialize_All();
            
            for (int j = 1; j <= Target_runcount; j++) {
            		
            	Initialize();

                Simulation_Main();
                
                Calculation(j);
                
            }
            
            System.out.println(Parameter_Value + " iterration complete\n\n\n");
            
            try {
                FinalResult();

            } catch (IOException ex) {
                Logger.getLogger(SDNUDP.class.getName()).log(Level.SEVERE, null, ex);
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
        UDP1_FLOWS_ACTIVE = new LinkedList<Integer>();
        UDP2_FLOWS_ACTIVE = new LinkedList<Integer>();
        UDP_FLOW_COUNT = new ArrayList<Integer>();
        
        Max_queue_length = 0;
   
        Total_switch_NUDP2 = 0;
        Total_switch_incoming = 0;
        Total_controller_incoming = 0;
        Total_switch_incoming_data_packet = 0;
        Total_switch_incoming_UDP_packet = 0;
        Total_switch_incoming_data_packet_source = 0;
        Total_switch_incoming_UDP_packet_source = 0;
        Total_switch_incoming_data_packet_controller = 0;
        Total_switch_incoming_UDP_packet_controller = 0;
		Total_controller_incoming_data_packet = 0;
		Total_controller_incoming_UDP_packet = 0;
		Total_served_data_packet = 0;
		Total_served_UDP_packet = 0;
		Total_served_switch = 0;
	    Total_served_controller = 0;
		Total_served_data_packet_switch_first = 0;
		Total_served_UDP_packet_switch_first = 0;
		Total_served_data_packet_switch_second = 0;
		Total_served_UDP_packet_switch_second = 0;
		Total_served_data_packet_controller = 0;
		Total_served_UDP_packet_controller = 0;
		Total_data_packet_dropped = 0;
		Total_UDP_packet_dropped = 0;
		Total_switch_packet_dropped = 0;
	    Total_controller_packet_dropped = 0;
		Total_data_packet_dropped_switch_first = 0;
		Total_UDP_packet_dropped_switch_first = 0;
		Total_data_packet_dropped_switch_second = 0;
		Total_UDP_packet_dropped_switch_second = 0;
		Total_data_packet_dropped_controller = 0;
		Total_UDP_packet_dropped_controller = 0;
		Total_UDP_termination = 0;
		Total_switch_queue_size = 0;
	    Total_controller_queue_size = 0;
	    Total_event_count = 0;
        
        Total_data_packet_delay = 0;
        Total_UDP_packet_delay = 0;
        
        UDP_FLOW_NUMBER = 0;
        UDP_FLOW_NUMBER++;
        
        First_UDP_packet_arrived_switch = false;
    	First_UDP_packet_arrived_controller = false;
    }

  //Variable initialization before each new parameter setting
    public static void Intialize_All() {
    	
    	avg_max_queue_length = 0;
    	Avg_switch_NUDP2 = 0;
    	Avg_switch_incoming = 0;
        Avg_controller_incoming = 0;
        Avg_switch_incoming_data_packet = 0;
        Avg_switch_incoming_UDP_packet = 0;
        Avg_switch_incoming_data_packet_source = 0;
        Avg_switch_incoming_UDP_packet_source = 0;
        Avg_switch_incoming_data_packet_controller = 0;
        Avg_switch_incoming_UDP_packet_controller = 0;
		Avg_controller_incoming_data_packet = 0;
		Avg_controller_incoming_UDP_packet = 0;
		Avg_served_data_packet = 0;
		Avg_served_UDP_packet = 0;
		Avg_served_switch = 0;
		Avg_served_controller = 0;
		Avg_served_data_packet_switch_first = 0;
		Avg_served_UDP_packet_switch_first = 0;
		Avg_served_data_packet_switch_second = 0;
		Avg_served_UDP_packet_switch_second = 0;
		Avg_served_data_packet_controller = 0;
		Avg_served_UDP_packet_controller = 0;
		Avg_data_packet_dropped = 0;
		Avg_UDP_packet_dropped = 0;
		Avg_switch_packet_dropped = 0;
	    Avg_controller_packet_dropped = 0;
		Avg_data_packet_dropped_switch_first = 0;
		Avg_UDP_packet_dropped_switch_first = 0;
		Avg_data_packet_dropped_switch_second = 0;
		Avg_UDP_packet_dropped_switch_second = 0;
		Avg_data_packet_dropped_controller = 0;
		Avg_UDP_packet_dropped_controller = 0;
		Avg_UDP_termination = 0;
		Avg_switch_queue_size = 0;
		Avg_controller_queue_size = 0; 
    	
        Mean_data_Delay = 0;
        Mean_UDP_Delay = 0;
        Mean_data_Drop = 0;
        Mean_UDP_Drop = 0;
        Mean_switch_drop = 0;
        Mean_controller_drop = 0;
    }

    static double Poisson_expon(double mean,String expon_type) {
    	
    	double rand = 0;
    	rand = Event_random.nextDouble();
    	
//Use in case of using Weibull distribution for Lambda_TCP Parameter    	
//    	if(expon_type.equals(SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)){
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
    	Total_switch_incoming_UDP_packet = Total_switch_incoming_UDP_packet_source;
    	Total_switch_queue_size = Total_switch_queue_size/Total_event_count;
    	Total_controller_queue_size = Total_controller_queue_size/Total_event_count;
    	Total_switch_NUDP2 = Total_switch_NUDP2 / Total_event_count;
    			
    	avg_max_queue_length += Max_queue_length;
    	
    	Avg_switch_NUDP2 +=  Total_switch_NUDP2;
    	Avg_switch_incoming += Total_switch_incoming;
        Avg_controller_incoming += Total_controller_incoming;
        Avg_switch_incoming_data_packet += Total_switch_incoming_data_packet;
        Avg_switch_incoming_UDP_packet += Total_switch_incoming_UDP_packet;
        Avg_switch_incoming_data_packet_source += Total_switch_incoming_data_packet_source;
        Avg_switch_incoming_UDP_packet_source += Total_switch_incoming_UDP_packet_source;
        Avg_switch_incoming_data_packet_controller += Total_switch_incoming_data_packet_controller;
        Avg_switch_incoming_UDP_packet_controller += Total_switch_incoming_UDP_packet_controller;
		Avg_controller_incoming_data_packet += Total_controller_incoming_data_packet;
		Avg_controller_incoming_UDP_packet += Total_controller_incoming_UDP_packet;
		
		Avg_served_data_packet += Total_served_data_packet;
		Avg_served_UDP_packet += Total_served_UDP_packet;
		Avg_served_switch += Total_served_switch;
		Avg_served_controller += Total_served_controller;
		Avg_served_data_packet_switch_first += Total_served_data_packet_switch_first;
		Avg_served_UDP_packet_switch_first += Total_served_UDP_packet_switch_first;
		Avg_served_data_packet_switch_second += Total_served_data_packet_switch_second;
		Avg_served_UDP_packet_switch_second += Total_served_UDP_packet_switch_second;
		Avg_served_data_packet_controller += Total_served_data_packet_controller;
		Avg_served_UDP_packet_controller += Total_served_UDP_packet_controller;
		Avg_data_packet_dropped += Total_data_packet_dropped;
		Avg_UDP_packet_dropped += Total_UDP_packet_dropped;
		Avg_switch_packet_dropped += Total_switch_packet_dropped;
	    Avg_controller_packet_dropped += Total_controller_packet_dropped;
		Avg_data_packet_dropped_switch_first += Total_data_packet_dropped_switch_first;
		Avg_UDP_packet_dropped_switch_first += Total_UDP_packet_dropped_switch_first;
		Avg_data_packet_dropped_switch_second += Total_data_packet_dropped_switch_second;
		Avg_UDP_packet_dropped_switch_second += Total_UDP_packet_dropped_switch_second;
		Avg_data_packet_dropped_controller += Total_data_packet_dropped_controller;
		Avg_UDP_packet_dropped_controller += Total_UDP_packet_dropped_controller;
		Avg_UDP_termination += Total_UDP_termination;
		Avg_switch_queue_size += Total_switch_queue_size;
		Avg_controller_queue_size += Total_controller_queue_size;
		
		
		Mean_data_Delay += Total_data_packet_delay/Total_served_data_packet;
        Mean_UDP_Delay += Total_UDP_packet_delay/Total_served_UDP_packet;
        Mean_data_Drop += (double)((double)Total_data_packet_dropped/(Total_switch_incoming_data_packet));
        Mean_UDP_Drop += (double)((double)Total_UDP_packet_dropped/(Total_switch_incoming_UDP_packet));
        Mean_switch_drop += (double)((double)Total_switch_packet_dropped/(Total_switch_incoming_data_packet+Total_switch_incoming_UDP_packet));
        Mean_controller_drop += (double)((double)Total_controller_packet_dropped/(Total_controller_incoming));
    	
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_Data_Delay_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Total_data_packet_delay/Total_served_data_packet);
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_UDP_Delay_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Total_UDP_packet_delay/Total_served_UDP_packet);
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_Data_Drop_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + (double)((double)Total_data_packet_dropped/(Total_switch_incoming_data_packet)));
            
        }
    	
    	try (FileWriter fw = new FileWriter(PATH+"All_UDP_Drop_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + (double)((double)Total_UDP_packet_dropped/(Total_switch_incoming_UDP_packet)));
            
        }
    	
    	SDN_EVENT_PriorityQueue.clear();
    	Switch_to_Controller_Line.clear();
    	Controller_to_Switch_Line.clear();
    	UDP1_FLOWS_ACTIVE.clear();
    	UDP2_FLOWS_ACTIVE.clear();
    	UDP_FLOW_COUNT.clear();
    }

    public static void FinalResult() throws IOException {
        	
        try (FileWriter fw = new FileWriter(PATH+"Data_Delay_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_data_Delay / Target_runcount);
            
        }
        
        try (FileWriter fw = new FileWriter(PATH+"Data_Drop_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_data_Drop / Target_runcount);
        } 
        
        try (FileWriter fw = new FileWriter(PATH+"UDP_Delay_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_UDP_Delay / Target_runcount);
        } 
        
        try (FileWriter fw = new FileWriter(PATH+"UDP_Drop_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_UDP_Drop / Target_runcount);
        }
        
        try (FileWriter fw = new FileWriter(PATH+"Switch_Controller_Drop_UDP.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(Parameter_Value + "    " + Mean_switch_drop / Target_runcount+ "    "+Mean_controller_drop/ Target_runcount);
        }
        
        try (FileWriter fw = new FileWriter(PATH+"UDP_DATA_STATISTICS.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
        	out.println("Parameters : \n");
        	out.println("Lembda_P : "+Lembda_P);
        	out.println("Lembda_UDP : "+Lembda_UDP);
        	out.println("Switch_MEU : "+Switch_MEU);
        	out.println("Controller_MEU : "+Controller_MEU);
        	out.println("Switch_MEU_UDP : "+Switch_MEU_UDP);
        	out.println("DSC : "+DSC);
        	out.println("Max_SwitchLength : "+Max_SwitchLength);
        	out.println("Max_ControllerLength : "+Max_ControllerLength);
        	out.println("Maximum_data_packet : "+Maximum_data_packet);
        	out.println("Simulation_time : "+Simulation_time);
        	out.println("Target_runcount : "+Target_runcount);
        	out.println("Parameter_Name : "+Parameter_Name);
        	out.println("---------------------------------------------");
        	out.println("\n\nParameter_Name : "+Parameter_Name);
            out.println(Parameter_Name+" : "+Parameter_Value);
            out.println("\nPacket Incoming :\nAvg_switch_incoming : "+Avg_switch_incoming/ Target_runcount);
            out.println("Avg_controller_incoming : "+Avg_controller_incoming/ Target_runcount);
            out.println("Avg_switch_incoming_data_packet : "+Avg_switch_incoming_data_packet/ Target_runcount);
            out.println("Avg_switch_incoming_UDP_packet : "+Avg_switch_incoming_UDP_packet/ Target_runcount);
            out.println("Avg_switch_incoming_data_packet_source : "+Avg_switch_incoming_data_packet_source/ Target_runcount);
            out.println("Avg_switch_incoming_UDP_packet_source : "+Avg_switch_incoming_UDP_packet_source/ Target_runcount);
            out.println("Avg_switch_incoming_data_packet_controller : "+Avg_switch_incoming_data_packet_controller/ Target_runcount);
            out.println("Avg_switch_incoming_UDP_packet_controller : "+Avg_switch_incoming_UDP_packet_controller/ Target_runcount);
            out.println("Avg_controller_incoming_data_packet : "+Avg_controller_incoming_data_packet/ Target_runcount);
            out.println("Avg_controller_incoming_UDP_packet : "+Avg_controller_incoming_UDP_packet/ Target_runcount);
            out.println("\nPacket Service :\nAvg_served_data_packet : "+Avg_served_data_packet/ Target_runcount);
            out.println("Avg_served_UDP_packet : "+Avg_served_UDP_packet/ Target_runcount);
            out.println("Avg_served_switch : "+Avg_served_switch/ Target_runcount);
            out.println("Avg_served_controller : "+Avg_served_controller/ Target_runcount);
            out.println("Avg_served_data_packet_switch_first : "+Avg_served_data_packet_switch_first/ Target_runcount);
            out.println("Avg_served_UDP_packet_switch_first : "+Avg_served_UDP_packet_switch_first/ Target_runcount);
            out.println("Avg_served_data_packet_switch_second : "+Avg_served_data_packet_switch_second/ Target_runcount);
            out.println("Avg_served_UDP_packet_switch_second : "+Avg_served_UDP_packet_switch_second/ Target_runcount);
            out.println("Avg_served_data_packet_controller : "+Avg_served_data_packet_controller/ Target_runcount);
            out.println("Avg_served_UDP_packet_controller : "+Avg_served_UDP_packet_controller/ Target_runcount);
            out.println("\nPacket Drop :\nAvg_data_packet_dropped : "+Avg_data_packet_dropped/ Target_runcount);
            out.println("Avg_UDP_packet_dropped : "+Avg_UDP_packet_dropped/ Target_runcount);
            out.println("Avg_switch_packet_dropped : "+Avg_switch_packet_dropped/ Target_runcount);
            out.println("Avg_controller_packet_dropped : "+Avg_controller_packet_dropped/ Target_runcount);
            out.println("Avg_data_packet_dropped_switch_first : "+Avg_data_packet_dropped_switch_first/ Target_runcount);
            out.println("Avg_UDP_packet_dropped_switch_first : "+Avg_UDP_packet_dropped_switch_first/ Target_runcount);
            out.println("Avg_data_packet_dropped_switch_second : "+Avg_data_packet_dropped_switch_second/ Target_runcount);
            out.println("Avg_UDP_packet_dropped_switch_second : "+Avg_UDP_packet_dropped_switch_second/ Target_runcount);
            out.println("Avg_data_packet_dropped_controller : "+Avg_data_packet_dropped_controller/ Target_runcount);
            out.println("Avg_UDP_packet_dropped_controller : "+Avg_UDP_packet_dropped_controller/ Target_runcount);
            out.println("\nOthers : \nAvg_switch_queue_size : "+Avg_switch_queue_size/ Target_runcount);
            out.println("Avg_controller_queue_size : "+Avg_controller_queue_size/ Target_runcount);
            out.println("Avg_UDP_termination : "+Avg_UDP_termination/ Target_runcount); 
            out.println("Avg_switch_NUDP2 : "+Avg_switch_NUDP2/ Target_runcount); 
            out.println("\n\n--------------------#####################---******---#####################--------------------\n\n");
        }
        
        System.out.println("Max queue length : "+avg_max_queue_length/Target_runcount);      
    }
     
    //Data Packet arrival event at Switch from Source
    private static void Handle_Data_Arrival(SDN_EVENT sdn_event) {    	
    	        
    	double simu_time = sdn_event.get_Event_Time();		
    	int flow_no = sdn_event.getFLOW_NUMBER();
    	
        Packet packet = new Packet(DATA_PACKET, simu_time,flow_no);

        SDN_switch.switchqueue.add(packet);
        	
       	double next_data_arrival = simu_time + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival,flow_no));
                    
    }

  //UDP Packet arrival at Switch from Source event handle
    private static void Handle_UDP_Arrival(SDN_EVENT sdn_event) {
    	
    	double simu_time = sdn_event.get_Event_Time(); 
    	int flow_no = sdn_event.getFLOW_NUMBER();
        
        SDN_switch.Nudp1count++;
        UDP1_FLOWS_ACTIVE.add(sdn_event.getFLOW_NUMBER());
        
        Packet packet = new Packet(UDP_PACKET, simu_time, false,flow_no);
        SDN_switch.switchqueue.add(packet);       

        UDP_FLOW_COUNT.add(0);
        
        UDP_FLOW_NUMBER++;
        double next_udp_arrival_time = simu_time + Poisson_expon(Lembda_UDP,SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
    	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_udp_arrival_time,UDP_FLOW_NUMBER));
    		
        double next_udp_terminate  = simu_time + Poisson_expon(Switch_MEU_UDP,SDN_EVENT.UDP_RELINQUISH_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.UDP_RELINQUISH_EVENT, next_udp_terminate,flow_no));
         

    	if(!First_UDP_packet_arrived_switch){
        	
        	double next_switch_departure = simu_time + Poisson_expon(Switch_MEU,SDN_EVENT.SWITCH_SERVICE_EVENT);
            SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SWITCH_SERVICE_EVENT, next_switch_departure));
            
            First_UDP_packet_arrived_switch = true;
        }
    	
    	double next_data_arrival_time = simu_time + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival_time,flow_no));
        
    }
    
  // Packet arrival at Switch from Controller event handle
    private static void Handle_Packet_Arrival_from_Controller(SDN_EVENT sdn_event, Packet packet) {
    	
    	double simu_time = sdn_event.get_Event_Time();
    	
    	if(packet.getPacket_type().equals(UDP_PACKET)){
    		
    		SDN_switch.Nudp1count--;
    		SDN_switch.Nudp2count++;
    		UDP1_FLOWS_ACTIVE.remove(new Integer(packet.getFlow_no()));
    		UDP2_FLOWS_ACTIVE.add(packet.getFlow_no());
    	}
    	
    	else{
    		
    		int flow_no = packet.getFlow_no();
    		if(UDP1_FLOWS_ACTIVE.contains(new Integer(flow_no))){
    			
    			SDN_switch.Nudp1count--;
        		SDN_switch.Nudp2count++;
        		UDP1_FLOWS_ACTIVE.remove(new Integer(packet.getFlow_no()));
        		UDP2_FLOWS_ACTIVE.add(packet.getFlow_no());
    		}
    	}
    	
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
        	
        	if(packet.ControllerVisited){
    			
    			packet.set_departure_second_time_switch(simu_time);
                Total_data_packet_delay += packet.Total_data_delay();
                
                Total_served_data_packet++;
                Total_served_data_packet_switch_second++;
                
    		
        	}else{
    			
    			if(UDP1_FLOWS_ACTIVE.contains(new Integer(packet.getFlow_no()))){
            		
    				packet.set_departure_time_switch(simu_time);
                    Switch_to_Controller_Line.add(packet);
                    
                    Total_served_data_packet_switch_first++;                  
                    
                    sdn_event.set_Event_Time(simu_time+ (1.0/DSC));
                    sdn_event.set_Event_Type(SDN_EVENT.PACKET_ARRIVAL_AT_CONTROLLER_EVENT);
                    SDN_EVENT_PriorityQueue.add(sdn_event);
            		
            	}else{
            		
            		packet.set_departure_time_switch(simu_time);
            		Total_data_packet_delay += packet.Total_data_delay();
            		
                    Total_served_data_packet++;
                    Total_served_data_packet_switch_first++;
                    
            	}
    		}
        	
        }   
        else{
        	
    		if(packet.ControllerVisited) {
            	
                packet.set_departure_second_time_switch(simu_time);
                Total_UDP_packet_delay += packet.Total_SYN_delay();
                 
                Total_served_UDP_packet++;
                Total_served_UDP_packet_switch_second++;     
                
                
             }else { 
                 
                 packet.set_departure_time_switch(simu_time);
                 Switch_to_Controller_Line.add(packet);
                                
                 Total_served_UDP_packet_switch_first++;
                 
                 sdn_event.set_Event_Time(simu_time+ (1.0/DSC));
                 sdn_event.set_Event_Type(SDN_EVENT.PACKET_ARRIVAL_AT_CONTROLLER_EVENT);
                 SDN_EVENT_PriorityQueue.add(sdn_event);
             }        	
        }

    	double next_switch_departure = simu_time + Poisson_expon(Switch_MEU,SDN_EVENT.SWITCH_SERVICE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.SWITCH_SERVICE_EVENT, next_switch_departure));
        
    }

  //UDP flow terminate event handle
    private static void Handle_UDP_departure(SDN_EVENT sdn_event) {
    	      
    	Total_UDP_termination++;
		SDN_switch.Nudp2count--;
		
		UDP2_FLOWS_ACTIVE.remove(new Integer(sdn_event.getFLOW_NUMBER()));
				
    }
   
  //Packet arrival at Controller from Switch event handle
    private static void Handle_Controller_Arrival(SDN_EVENT sdn_event, Packet packet) {
    	
    	 
    	double simu_time = sdn_event.get_Event_Time();
    	
    	packet.set_arrival_time_controller(simu_time);
    	
    	SDN_controller.controllerQueue.add(packet);
    	
    	if(!First_UDP_packet_arrived_controller){
    		
    		double next_controller_departure = simu_time + Poisson_expon(Controller_MEU,SDN_EVENT.CONTROLLER_SERVICE_EVENT);
    		SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.CONTROLLER_SERVICE_EVENT, next_controller_departure));
            
            First_UDP_packet_arrived_controller = true;
    	}
    	
    }
    
    //Controller service event handle
    private static void Handle_Controller_Departure(SDN_EVENT sdn_event) {
    	
        double simu_time = sdn_event.get_Event_Time();
        
        Packet packet = SDN_controller.controllerQueue.remove();
        
        packet.set_departure_time_controller(simu_time);
        
        Controller_to_Switch_Line.add(packet);
       	
       	sdn_event.set_Event_Time(simu_time + (1.0/DSC));
   		sdn_event.set_Event_Type(SDN_EVENT.PACKET_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT);
   		SDN_EVENT_PriorityQueue.add(sdn_event);
        
   		Total_served_controller++;
   		if(packet.getPacket_type().equals(DATA_PACKET)){
   			Total_served_data_packet_controller++;
   		}else{
   			Total_served_UDP_packet_controller++;
   		}
       	       	
        double next_controller_departure = simu_time + Poisson_expon(Controller_MEU,SDN_EVENT.CONTROLLER_SERVICE_EVENT);
        SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.CONTROLLER_SERVICE_EVENT, next_controller_departure));
    }
    
    //Main Simulation Program
    private static void Simulation_Main() {
    	
    	double udp_arrival_time = Poisson_expon(Lembda_UDP,SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
    	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, udp_arrival_time,UDP_FLOW_NUMBER)); 
    	
    	Max_queue_length = -1;
        while (Total_switch_incoming_data_packet_source < Maximum_data_packet) {	
          
    	if(Max_queue_length<SDN_switch.switchqueue.size()){
    		Max_queue_length = SDN_switch.switchqueue.size();
    	}
          Total_switch_queue_size += SDN_switch.switchqueue.size();
          Total_controller_queue_size +=SDN_controller.controllerQueue.size();
          Total_switch_NUDP2 += SDN_switch.Nudp2count;
          
          Total_event_count++;
          
          SDN_EVENT sdn_event = (SDN_EVENT)SDN_EVENT_PriorityQueue.poll();
          
          if(sdn_event.get_Event_Type().equals(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)) {

        	  if(UDP1_FLOWS_ACTIVE.contains(new Integer(sdn_event.getFLOW_NUMBER())) || UDP2_FLOWS_ACTIVE.contains(new Integer(sdn_event.getFLOW_NUMBER()))){
        		
        		  Total_switch_incoming++;
              	  Total_switch_incoming_data_packet_source++;
              	  
        		  if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
                  	
                  	Handle_Data_Arrival(sdn_event);
                  
                  } else {
                  	
                	Total_switch_packet_dropped++;
                  	Total_data_packet_dropped_switch_first++;
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

            }else if(sdn_event.get_Event_Type().equals(SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT)){

            	Total_switch_incoming++;
            	Total_switch_incoming_UDP_packet_source++;

                if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
                	
                	Handle_UDP_Arrival(sdn_event);

                } else {
                	
                	UDP1_FLOWS_ACTIVE.add(sdn_event.getFLOW_NUMBER());
                	UDP_FLOW_COUNT.add(0);
                	double next_data_arrival_time = sdn_event.get_Event_Time() + Poisson_expon(Lembda_P,SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
                    SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.DATA_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_data_arrival_time,sdn_event.getFLOW_NUMBER()));

                    double next_udp_terminate  = sdn_event.get_Event_Time() + Poisson_expon(Switch_MEU_UDP,SDN_EVENT.UDP_RELINQUISH_EVENT);
                    SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.UDP_RELINQUISH_EVENT, next_udp_terminate,sdn_event.getFLOW_NUMBER()));

                	Total_switch_packet_dropped++;
                	Total_UDP_packet_dropped_switch_first++;
                	Total_UDP_packet_dropped++;
                	UDP_FLOW_NUMBER++;
                	
                	double next_udp_arrival_time = sdn_event.get_Event_Time() + Poisson_expon(Lembda_UDP,SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT);
                	SDN_EVENT_PriorityQueue.add(new SDN_EVENT(SDN_EVENT.UDP_ARRIVAL_AT_SWITCH_FROM_SOURCE_EVENT, next_udp_arrival_time,UDP_FLOW_NUMBER));

                }

            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.PACKET_ARRIVAL_AT_SWITCH_FROM_CONTROLLER_EVENT)){
            	
            	Packet packet = Controller_to_Switch_Line.remove();
            	Total_switch_incoming++;
           		if(packet.getPacket_type().equals(DATA_PACKET)){
           			Total_switch_incoming_data_packet_controller++;
           		}else{
           			Total_switch_incoming_UDP_packet_controller++;
           		}
           		
            	if (SDN_switch.switchqueue.size() < Max_SwitchLength) {
        	 		
               		Handle_Packet_Arrival_from_Controller(sdn_event,packet);

                } else {
                	
                	Total_switch_packet_dropped++;
                	if(packet.getPacket_type().equals(UDP_PACKET)){
                		Total_UDP_packet_dropped++;
                		Total_UDP_packet_dropped_switch_second++;

                	}else{
                		Total_data_packet_dropped++;
                		Total_data_packet_dropped_switch_second++;
                	}
                	
                }
            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.UDP_RELINQUISH_EVENT)){
            	
            	if (UDP2_FLOWS_ACTIVE.contains(new Integer(sdn_event.getFLOW_NUMBER()))) {
                	
                    Handle_UDP_departure(sdn_event);  
                } 
            }
            else if(sdn_event.get_Event_Type().equals(SDN_EVENT.PACKET_ARRIVAL_AT_CONTROLLER_EVENT)){
            	
            	Packet packet = Switch_to_Controller_Line.remove(); 
                Total_controller_incoming++;
                
                if(packet.getPacket_type().equals(DATA_PACKET)){
                	Total_controller_incoming_data_packet++;
                }else{
                	Total_controller_incoming_UDP_packet++;
                }
                
                if (SDN_controller.controllerQueue.size() < Max_ControllerLength) {
               	 
                	Handle_Controller_Arrival(sdn_event,packet);

                }else {
                	
                	if(packet.getPacket_type().equals(DATA_PACKET)){
                    	Total_data_packet_dropped++;
                    	Total_data_packet_dropped_controller++;
                	}else{
                		Total_UDP_packet_dropped++;
                     	Total_UDP_packet_dropped_controller++;
                	}

                	Total_controller_packet_dropped++;
                	
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
