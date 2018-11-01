import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class InputValidation {
	 ArrayList<String[]> allData;
	 double time;
	 
	public void go() {
		importCSV();
		inputDataValues();
		ImportCSVFirst test = new ImportCSVFirst(allData, time);
	}
	
	private void importCSV() {
		allData = new ArrayList<String[]>();
	   String fileName = "12month_3909_3918.ascii";
		File file = new File(fileName);
  	   try {
			Scanner inputStream = new  Scanner(file);
			int countTotal = 0;
			while(inputStream.hasNext()) {
				String data = inputStream.next();
            String[] dataArray = {data, inputStream.next()}; //assumes every line will have two pieces of data 
			   countTotal++;
			   allData.add(dataArray);
			}
			System.out.println(countTotal);
		   inputStream.close();
		} 
      catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void inputDataValues() {
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Please enter the length of time for an apnea (in seconds): ");
		time = reader.nextDouble();
	}
}