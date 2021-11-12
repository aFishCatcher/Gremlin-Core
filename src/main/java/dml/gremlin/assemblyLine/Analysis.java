package dml.gremlin.assemblyLine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Analysis {

	public static void analysis() throws IOException {
		String[] paths= {"10Log.txt", "11Log.txt"};
		for(String path:paths) {
			Scanner in = new Scanner(Paths.get(path));
			Process compute = new Process(path + "compute");
			Process syn = new Process(path +"syn");
			int line_num = 0;
			while(in.hasNextLine())
			{
				line_num++;
				String line = in.nextLine();
				String[] datas = line.split(",");
				compute.process(datas[0], line_num);
				syn.process(datas[1], line_num);
			}
			compute.show();
			syn.show();
		}
		

	}

}

class Process{
	private String name;
	private long _1=0, _10=0, _100=0, _500=0;
	private long s_1=0, s_10=0, s_100 =0, s_500=0;
	private String message="";
	public Process(String _name) {
		this.name = _name;
	}
	void process(String data, int line) {
		try {
			long value = Long.valueOf(data);
			if(value/1_000>500) { _500++; s_500+=value; message+=line+ ", ";}
			else if(value/1_000 > 100) {_100++; s_100+=value;}
			else if(value/1_000 > 10) {_10++; s_10+=value;}
			else {_1++; s_1+=value;}
		}
		catch(NumberFormatException e) {
			System.out.println("-----"+data);
			return;
		}
	}
	public void show() {
		System.out.println("-------"+name+"-------");
		System.out.println(">500: "+_500+", "+s_500/1_000);
		System.out.println(">100: "+_100+", "+s_100/1_000);
		System.out.println(">10: "+_10+", "+s_10/1_000);
		System.out.println(">0: "+_1+", "+s_1/1_000);
		System.out.println(">500 at lines: "+message);
	}
}