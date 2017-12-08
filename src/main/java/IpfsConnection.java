import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class IpfsConnection {

    //all files and folder needs to be inside res/files directory
    private static final String filename = "bipin.txt";
    private static final String mp3filename = "MileyCyrus.mp3";
    private static final String directoryName = "bipinDir";
    private static byte[] content = "This is just a random line copied from somewhere to check the demo.".getBytes();

    private static IPFS ipfs;

    public static void main(String[] args) throws IOException {
         ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
         ipfs.refs.local();

         //file uplaod and view
        String filehash = addFileToIPFS(filename);
        System.out.println("File Uploaded. Identifier Hash = " + filehash);
        displayFileFromIPFS(filehash);

        //raw bytes upload and view
        String bytehash = addRawDataToIPFS(content);
        System.out.println("Raw bytes uploaded. Identifier Hash = " + filehash);
        displayFileFromIPFS(bytehash);

        //mp3 upload
        String mp3filehash = addFileToIPFS(mp3filename);
        System.out.println("MP3 Uploaded. Identifier Hash = " + mp3filehash);

        //directory upload
        String folderhash = addFolderToIPFS(directoryName);
        System.out.println("Directory Uploaded. Identifier Hash = " + folderhash);

    }


    public static String addFileToIPFS(String filename) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("./res/files/"+filename));
        MerkleNode addResult = ipfs.add(file).get(0);
        Multihash pointer = addResult.hash;
        return pointer.toString();
    }


    public static String addRawDataToIPFS(byte[] content) throws IOException {
        String name = "hello.txt";
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(name, content);
        MerkleNode addResult = ipfs.add(file).get(0);
        Multihash pointer =  addResult.hash;
        return pointer.toString();
    }

    public static void displayFileFromIPFS(String filehash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(filehash);
        byte[] fileContents = ipfs.cat(filePointer);
        System.out.println("File contents :: "+new String(fileContents));
    }


    public static String addFolderToIPFS(String foldername) throws IOException {
        NamedStreamable.DirWrapper dir = new NamedStreamable.DirWrapper(".res/files/"+foldername, Arrays.<NamedStreamable>asList());
        MerkleNode addResult = ipfs.add(dir).get(0);
        Multihash pointer = addResult.hash;
        return pointer.toString();
    }

}
