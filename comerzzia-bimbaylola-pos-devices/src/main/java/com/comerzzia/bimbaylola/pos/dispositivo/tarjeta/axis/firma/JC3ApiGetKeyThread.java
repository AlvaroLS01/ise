package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.firma;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import jline.ConsoleReader;

import org.apache.log4j.Logger;

import com.ingenico.fr.jc3api.JC3ApiConstants;

public class JC3ApiGetKeyThread extends Thread implements JC3ApiConstants{
	
	private Logger logger_;
	private LinkedList<Key> queue_;
	private ConsoleReader reader_;
	private ReentrantLock getStringLock_;

	static class Key{
		int key_;
		Key(int key){
			key_ = key;
		}
		int getKey(){
			return key_;
		}
	}

	public JC3ApiGetKeyThread(Logger logger) throws IOException{
		logger_ = logger;
		queue_ = new LinkedList<Key>();
		reader_ = new ConsoleReader();
		getStringLock_ = new ReentrantLock();
	}

	public void run(){
		while(!interrupted()){
			int key = 0;
			boolean keyAllowed = false;
			while(!keyAllowed){
				try{
					key = reader_.readVirtualKey();
				}catch(IOException e){
					logger_.error("JC3ApiGetKeyThread/run() - " + e.getMessage(), e);
					return;
				}
				/* ENTER or 'V' or 'v' */
				if(key == 10 || key == 13 || key == 86 || key == 118){
					key = C3KEY_VALIDATION;
					keyAllowed = true;
				/* ESC or 'A' or 'a */
				}else if(key == 27 || key == 65 || key == 97){
					key = C3KEY_CANCELLATION;
					keyAllowed = true;
				/* BACKSPACE or 'C' or 'c' */
				}else if(key == 8 || key == 67 || key == 99){
					key = C3KEY_CORRECTION;
					keyAllowed = true;
				/* Numeric */
				}else if(key >= 48 && key <= 57){
					keyAllowed = true;
				}
			}
			/* Add the key to the queue */
			putKey(key);
			/* Wait for get string lock */
			getStringLock_.lock();
			getStringLock_.unlock();
		}
	}

	public void putKey(int key){
		synchronized(queue_){
			queue_.addLast(new Key(key));
			queue_.notify();
		}
	}

	public int getKey(){
		Key key;
		synchronized(queue_){
            while(queue_.isEmpty()){
                try{
                    queue_.wait();
                }catch(InterruptedException e){
                	logger_.error("JC3ApiGetKeyThread/getKey() - " + e.getMessage(), e);
                }
            }
            key = queue_.removeFirst();
        }
		return key.getKey();
	}
	
	public int peekKey(){
		Key key;
		synchronized(queue_){
            while(queue_.isEmpty()){
                try{
                    queue_.wait();
                }catch(InterruptedException e){
                	logger_.error("JC3ApiGetKeyThread/peekKey() - " + e.getMessage(), e);
                }
            }
            key = queue_.peek();
        }
		return key.getKey();
	}

	public boolean hasKey(){
		synchronized(queue_){
            return (queue_.isEmpty() == false);
		}
	}
	
	public String getString(int max) throws IOException{
		getStringLock_.lock();

		StringBuilder sb = null;
		try{
			int key = getKey();
			/* get last key pressed */
			if(key != C3KEY_CANCELLATION){
				sb = new StringBuilder();
				if(key != C3KEY_VALIDATION){
					sb.append((char) key);
					sb.append(reader_.readLine(sb.toString()));
					if(sb.length() > max){
						String input = sb.toString();
						sb.delete(max, sb.length());
						logger_.debug("Max input length exceeded ! User input `" + input + 
							"' truncated --> `" + sb.toString() + "' !");
					}
					logger_.debug("> " + sb.toString());
				}
			}
		}finally{
			getStringLock_.unlock();
		}
		return (sb != null) ? sb.toString() : null;
	}

	public int getSecurity() throws IOException{
		logger_.debug("GET SECURITY ? (VAL/ESC)");
		return getKey() == C3KEY_VALIDATION ? C3KEY_VALIDATION:C3KEY_CANCELLATION;
	}

	public int getSalesConfirmation() throws IOException{
		logger_.debug("SALES CONFIRMATION ? (VAL/ESC)");
		return getKey() == C3KEY_VALIDATION ? C3KEY_VALIDATION:C3KEY_CANCELLATION;
	}
}
