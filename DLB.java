public class DLB {
	
	//Set as variable so it can be easily replaced.
	private final char terminator = '$';
	
	
	private DLBnode root;
	
	public DLB() {
		root=new DLBnode();
	}
	
	public boolean add(String key) {
		DLBnode currentNode=root;
		boolean result=true;
		key=key+terminator;
		
		for(int i=0;i < key.length();i++) {
			char keyChar=key.charAt(i);
			//No child node is present so a new child is created.
			if(currentNode.getChild()==null) {
				currentNode.setChild(new DLBnode(keyChar));
				currentNode=currentNode.getChild();
			}
			//There already exists a child node so a sibling must be added to the child node.
			else{
				//The current node is now set to the child of the previous parent.
				currentNode=currentNode.getChild();
				
				while(currentNode.hasSibling()) {
					if(currentNode.key==keyChar) {
						break;	//Current sibling matches the key trying to be inserted
					}
					currentNode=currentNode.getSibling();
				}
				
				//Basically the check inside the loop but this one changes result and checks the final sibling.
				if(currentNode.key==keyChar) {
					result=false;	//The key was already a sibling
				}
				//inserts the key as a sibling otherwise.
				else {
					result=true;
					currentNode.setSibling(new DLBnode(keyChar));
					currentNode=currentNode.getSibling();
				}
			}
			
		}
		//Returns the result of whether the final key was in the DLB or not.
		return result;
	}
	
	//Searches DLB for string s and returns node at which a result was found.
	//NOTE: will return the node for 'h'-'e'-'l' when string "hel" is entered despite "hel" not being a full word.
	public DLBnode search(String key) {
		DLBnode currentNode=root;
		
		for(int i=0;i < key.length();i++) {
			char keyChar=key.charAt(i);
			currentNode=currentNode.getChild();
			//Loops through all siblings until it finds a null 
			while(currentNode!=null) {
				//System.out.println(currentNode.key+" "+keyChar);
				if(currentNode.key==keyChar)	break;
				currentNode=currentNode.getSibling();
			}
			if(currentNode==null) {
				return null;
			}
			
		}
		
		return currentNode;
	}
	

	//returns at most 5 suggestions based on input
	public String[] suggest(String input) {
		String[] results = new String[5];
		//A temporary root node used to act as a starting point for finding words.
		//Finds a start point within the DLB
		DLBnode startPoint=search(input);
		if(startPoint==null) {
			return results;
		}
		startPoint=startPoint.getChild();
		
		for(int i=0;i<results.length;i++) {
			if(startPoint==null)	return results;
			DLBnode currentNode=startPoint;
			results[i]=input;
			while(!currentNode.isTerminator()) {
				results[i]+=currentNode.key;
				currentNode=currentNode.getChild();
			}
			//After one word has been located begin looking for other siblings to latch onto.
			currentNode=startPoint;
			//go down a layer until a sibling is found or it runs out of children.
			while(!currentNode.hasSibling() && currentNode.getChild() != null) {
				currentNode=currentNode.getChild();
			}
			startPoint=currentNode.getSibling();
		}
		
		
		
		
		return results;
	}
	
	//Small helper method to prevent duplicates.
	//True if the array a contains string s.
	private boolean arrayContains(String[] a, String s) {
		if(a==null)	return false;
		for(int i=0;i<a.length;i++) {
			if(a[i]!=null && a[i].equals(s))	return true;
		}
		return false;
	}
	
	//if previous array isn't full attempts to fill it with suggestions from this trie.
	public String[] suggest(String input, String[] previous) {
		int nullPos;
		//Finds first position for null entry.
		for(nullPos=0;nullPos<previous.length;nullPos++) {
			if(previous[nullPos]==null)		break;
		}
		//A temporary root node used to act as a starting point for finding words.
		//Finds a start point within the DLB
		DLBnode startPoint=search(input);
		if(startPoint==null)		return previous;
		startPoint=startPoint.getChild();
		
		for(int i=nullPos;i<previous.length;i++) {
			if(startPoint==null)	return previous;
			DLBnode currentNode=startPoint;
			String word=input;
			while(!currentNode.isTerminator()) {
				word+=currentNode.key;
				currentNode=currentNode.getChild();
			}
			if(arrayContains(previous,word)) 	i--;
			else	previous[i]=word;
			//After one word has been located begin looking for other siblings to latch onto.
			currentNode=startPoint;
			//go down a layer until a sibling is found or it runs out of children.
			while(!currentNode.hasSibling() && currentNode.getChild() != null) {
				currentNode=currentNode.getChild();
			}
			startPoint=currentNode.getSibling();
		}
		return previous;
	}
	



	private class DLBnode{
		private DLBnode sibling;
		private DLBnode child;
		private char key;
		
		public DLBnode() {
			
		}
		public DLBnode(char key) {
			this.key=key;
		}
		
		/*
		public DLBnode(String s) {
			key=s.charAt(0);
			sibling=null;
			//If it is the terminator character, flags node as a dead end.
			isTerminator=(key==terminator);
			//Exit case
			if(isTerminator) {
				child=null;
			}
			//Otherwise continues to create child nodes recursively 
			else {
				child=new DLBnode(s.substring(1, s.length()));
			}
		}
		*/
		
		public boolean hasSibling() {
			return sibling!=null; 
		}
		
		public boolean isTerminator() {
			return (key==terminator);
		}

		public DLBnode getChild() {
			return child;
		}

		public void setChild(DLBnode child) {
			this.child = child;
		}

		public DLBnode getSibling() {
			return sibling;
		}

		public void setSibling(DLBnode sibling) {
			this.sibling = sibling;
		}
	}

}
