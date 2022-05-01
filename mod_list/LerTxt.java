import java.io.*;

public class LerTxt {  
    public static void main(String args[]) {  
        try {  
            File file=new File("hunter.txt");    //creates a new file instance  
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
            String line;  
            while((line=br.readLine())!=null) {
                System.out.println(line + "---");
                if (line == "Mod Title:\n" ){
                    System.out.println("Linha desejada encontrada!!!");
                }
                sb.append(line);      //appends line to string buffer
                sb.append("\n");     //line feed
            }  
            fr.close();    //closes the stream and release the resources  
            System.out.println("Contents of File: ");  
            System.out.println(sb.toString());   //returns a string that textually represents the object  
        }  
        catch(IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  