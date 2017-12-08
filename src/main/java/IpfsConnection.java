import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.IOException;

public class IpfsConnection {

    //all files need to be inside res/files directory
    private static final String filename = "bipin.txt";

    private static IPFS ipfs;

    public static void main(String[] args) throws IOException {
         ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
         ipfs.refs.local();

         //file uplaod
        String filehash = addFileToIPFS(filename); //Qmd1GGzoLtzkZXuJ29i87fX2NQfcv8pWLZJ8BBgVXzRJbA
        System.out.println("File Uploaded. Identifier Hash = " + filehash);
        //ipfs file content display
        displayFileFromIPFS(filehash);
        
    }

    public static String addFileToIPFS(String filename) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("./res/files/"+filename));
        MerkleNode addResult = ipfs.add(file).get(0);
        Multihash pointer = addResult.hash;
        return pointer.toString();
    }

    public static void displayFileFromIPFS(String filehash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(filehash);
        byte[] fileContents = ipfs.cat(filePointer);
        System.out.println("\n \n File contents ::\n"+new String(fileContents));
    }


}
