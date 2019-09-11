package com.example.parkingman.Storage;

import android.content.Context;

import com.example.parkingman.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

/**
 * A class that exposes Static and instance methods to
 * manage the private internal storage for the current app.
 * The internal storage is not accessible by other apps,
 * but whenever this app is removed from the users system
 * all files will go with it. Use this to store private
 * information that user can afford to lose.
 * */
public class InternalStorage {

    private final String filename;

    public InternalStorage(String filename){
        this.filename = filename;
    }

    public boolean exists(String filename){
        File f = new File(filename);
        return f.exists();
    }

    public boolean exists(){
        return exists(this.filename);
    }

    /**
     * Writes a string to a file in the Internal Storage of this app.
     */
    public static void write(String filename, String data){
        try(FileOutputStream fs = App.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter sw = new OutputStreamWriter(fs)
        ){
            sw.write(data);
        } catch (IOException e) {
            e.printStackTrace(); // File cannot be accessed for unknown reason
        }
    }

    /**
     * Serializes an object to a file in the Internal Storage of this app.
     * Returns true if operation succeeds. False if fails.
     */
    public static boolean writeObject(Object object, String filename){
        try(FileOutputStream fs = App.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fs)
        ){
            os.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reads a serialized object from a file in the Internal Storage of this app.
     */
    public static Object readObject(String filename){
        try(FileInputStream fs = App.getContext().openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fs)
        ){
            return is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Serializes an object to a file in the Internal Storage of this app.
     * Returns true if operation succeeds. False if fails.
     */
    public boolean writeObject(Object object){
        return writeObject(object, this.filename);
    }

    /**
     * Reads a serialized object from a file in the Internal Storage of this app.
     */
    public Object readObject(){
        return readObject(this.filename);
    }


    /**
     * Writes a string to a file in the Internal Storage of this app.
     */
    public void write(String data) {
        write(this.filename, data);
    }

    /**
     * Reads a file from the Internal Storage of this app.
     */
    public static String read(String filename) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        try(FileInputStream fs = App.getContext().openFileInput(filename);
            InputStreamReader sr = new InputStreamReader(fs);
            BufferedReader br = new BufferedReader(sr)
        ){
            String readline;
            do{
                readline = br.readLine();
                sb.append(readline);
            } while (readline != null);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace(); // File cannot be accessed for unknown reason
        }
        return sb.toString();
    }

    /**
     * Reads a file from the Internal Storage of this app.
     * */
    public String read() throws FileNotFoundException {
        return read(this.filename);
    }

}
