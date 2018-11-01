import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class ImportCSVFirst {

   private ArrayList<String[]> allData;

   public ImportCSVFirst() {
      allData = new ArrayList<String[]>();   
   }

   public void go() {
      importCSV();
      System.out.println("DONE");
		deleteBadData();
      //checkTimeLength();
      writeDataToTextFile("test_01.txt");
      System.out.println();
      isNotNull();
      System.out.println();
		locateGoodData();
      System.out.println();
      findRange();
      System.out.println("DONE");
   }
   
	private void importCSV() {
	   String fileName = "12mo_3913_3118marked.txt";
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
	
	private void deleteBadData() {
		for (int i = 0; i < allData.size()-1; i++) {
			if (Double.parseDouble((allData.get(i)[1]))<.15 && Double.parseDouble((allData.get(i)[1]))>-.15) {
			   //might want to do something here, blank for now
			} 
         else {
				allData.get(i)[1] = null;
			}
		}
		System.out.println("Finished, " + allData.size());
	}
   
   private void checkTimeLength() {
      int counter = 0; 
      int startIdx = 0; 
      for (int i = 1; i < allData.size()-1; i++) {
			if (allData.get(i)[1] == null) {
				if(!(isNull(i-1))) {
					 int length = i - startIdx;
					 if (length < 125) {
					 	changeToNull(startIdx, i);
					 } 
				}
			}
			else {
				if(isNull(i-1)) {
					startIdx = i;
				}
			}
		}
   }
	
	private boolean isNull(int idx) {
		if(allData.get(idx)[1] == null) {
		   return true;
      }
		else {
			return false;
      }		
	} 
	
	private void changeToNull(int start, int end) {
		for (int i = start; i <= end; i++) {
			allData.get(i)[1] = null;
		}
	}
   
	private void locateGoodData() {
	int goodCounter = 0;
	int badCounter = 0;
      for (int i = 0; i < allData.size()-1; i++) {
		   if(allData.get(i)[1] != null) {
				//System.out.print("Beggining of range: " + i);
				int end = locateEndOfRange(i);
				int range = end - i;
				if(range < 125)
					badCounter++;
				else {
					goodCounter++;	
					System.out.println(allData.get(i)[0] + " " + range);
				}
				//System.out.println(" End of range: " + end);
				i = end;
         }
		}
		System.out.println("Good: " + goodCounter + " Bad: " + badCounter);
	}
	
	private int locateEndOfRange(int idx) {
		 while(allData.get(idx)[1] != null){
		 	idx++;
		 }
		 return idx;
	}
	
   private void findRange() {
      int a = 0;
      int b = 0;
      
      for (int i = 0; i < allData.size()-1; i++) {
         if (allData.get(i)[1] != null) {
            a = i;
            for (int j = i; j < allData.size()-1; j++) {
               if (allData.get(j)[1] == null) {
                  b = j-1;
                  i = j;
                  j = allData.size();
               } 
            } 
				if(b-a >= 125) 
          	  if (checkInRange(a+1, b-1)) {
               System.out.println("Range: " + allData.get(a)[0] + " " + b);   
            } 
            else {
               if ((b-a) > 125) {
                  int c = a + 125;
                  int d = a;
                  while (c <= b) {
                     if (checkInRange(d, c)) {
                        System.out.println("APNEA " + d + " " + c);
								if(b-c < 125 )
                       		 c = b+1;
								else {
									d=c+1;
									c = d + 125;
								}	 
                        //if there is more than 125 left, check for more apneas in range.
                     } 
                     else {
                        d++;
                        c++;
                     }
                  }
               }
               System.out.println("Not in range " + (a+1) + " " + (b+1) + " " + (b - a)); 
            }
         } 
      }   
   }
   
   private boolean checkInRange(int a, int b) {
      int min = a;
      int max = a;
		//Added this if because null pointer was thrown at 165 after changing logic where this method is called
		//Shouldn't need this if eventually.
      if(allData.get(a)[1] != null && allData.get(b)[1] != null && b-a >=125){
         for (int i = a; i <= b; i++) {
            if (Double.parseDouble((allData.get(i)[1])) < Double.parseDouble(allData.get(min)[1])) {
               min = i;
            }   
            if (Double.parseDouble(allData.get(i)[1]) > Double.parseDouble(allData.get(max)[1])) {
               max = i;
            }   
         } 
                
         if (Double.parseDouble(allData.get(max)[1]) - Double.parseDouble(allData.get(min)[1]) > 0.025) {
            return true;
         }
         else {
            return true;
         }    
		}
		else
			return false;
   }  
     
   public void writeDataToTextFile(String file) {
      PrintWriter pw = null;
      FileWriter outputFile = null;
      File afile = new File(file);
      try {
         outputFile = new FileWriter(file);
         pw = new PrintWriter(outputFile);
         for (int i = 0; i < allData.size()-1; i++) {
            pw.write(allData.get(i)[0] + " " + allData.get(i)[1] + "\n");
         } 
      } 
      catch (IOException e) {
         e.printStackTrace();
      } 
   }
   
   private void isNotNull() {
      int counter = 0;
      for (int i = 0; i < allData.size(); i++) {
         if (allData.get(i)[1] != null) {
            counter++;
         }
      }
      System.out.println("Non-null values: " + counter);
   }
   
   private void display() {
      for (int i = 0; i < allData.size()-1; i++) {
         System.out.println(allData.get(i)[0] + " " + allData.get(i)[1]);
      }  
   }
}