import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;




public class Machine_Learning_Hw5 {

	public static int MAX_ATTRIBUTES = 0;
	public Machine_Learning_Hw5() {
		// TODO Auto-generated constructor stub
	}
	
	public static Machine_Learning_Hw5 hw1 = new Machine_Learning_Hw5();
	static ArrayList<ArrayList<Record>> trainingRecords = new ArrayList<ArrayList<Record>>();
	static ArrayList<Record> testRecords = new ArrayList<Record>();
	static ArrayList<ArrayList<Feature>> attributes = new ArrayList<ArrayList<Feature>>();
	static ArrayList<Node> roots = new ArrayList<Node>();
	static ArrayList<Tree> trees = new ArrayList<Tree>();
	

	public static void main(String[] args) {
		// There should be exact 2 arguments
		
		if (args.length != 2) {
			System.out.println("You should give exactly 2 argumetns as input which are names of training data and test data files");
			System.exit(1);

		}
		//System.out.println("Programm in Execution");
		String trainingDataPath = args[0];
		String testDataPath = args[1];
		
		
		File trainingData = new File(trainingDataPath);
		File testData = new File(testDataPath);
		boolean readFlag = false;
		// Need the following flags while reading file
		boolean isTrainingrecord = true;
		boolean isTestrecord = false;
		int trainingSet = 0;
		for (File file: trainingData.listFiles())
		{
			ArrayList<Feature> usedAttributes = new ArrayList<Feature>();
			//Path should not contain any sub folders
			if(file.isDirectory())
			{
				System.out.println("Something is wrong. Can't have subfolders");
			}
		
		readFlag = false;
		//Create new Record set for each model to be built
		ArrayList<Record> trainingRecord = new ArrayList<Record>();
		//Add it to the trainingRecords which contains training record for each model
		trainingRecords.add(trainingRecord);
		readFlag = hw1
				.ReadFile(file, trainingRecords.get(trainingSet), isTrainingrecord, trainingSet);
		if (readFlag == false) {
			System.out.println("Error reading File.");
			System.exit(1);
		}
		
		Feature attribute;
		for (int i = 0; i < attributes.get(trainingSet).size(); i++) {
			attribute = attributes.get(trainingSet).get(i);
			//System.out.print(" " + attribute.name + " " + attribute.MAX);
		}
		Node root = hw1.new Node(trainingRecords.get(trainingSet));
		Tree tree = hw1.new Tree();
		//Add each tree to the list of trees
		trees.add(tree);
		for (int i = 0; i < attributes.get(trainingSet).size(); i++) {
			String temp = attributes.get(trainingSet).get(i).name;
		//	System.out.println("attribute " + temp);
		}
		Feature dummy = hw1.new Feature();
		dummy.name = "first";
		root.attribute = dummy;
		//add each root to the set of roots
		roots.add(root);
		root = tree.buildTree(trainingRecords.get(trainingSet), roots.get(trainingSet), attributes.get(trainingSet), usedAttributes, trainingSet);
		roots.set(trainingSet, root);
		//System.out.println();
		//Iterate next traiingSet
		trainingSet++;

		}
		
		//Test the file for each training Model
			readFlag = hw1.ReadFile(testData, testRecords, isTestrecord, 0);
			//double accuracyTestData = trees.get(i).findAccuracy(roots.get(i), testRecords,i);
		if (readFlag == false) {
			System.out.println("Error reading File for Test.");
			System.exit(1);
		
		}
		double accuracyTestData = trees.get(0).findAccuracy(roots, testRecords, trainingSet);
		//System.out.println("\nMaximum attributes for this training data is "
		//		+ MAX_ATTRIBUTES)
		//System.out.println("\nAttributes and their MAX  values are:");
		System.out.println("Number of training Sets in Bagging : " + trainingSet);
		//System.out.println("\nAccuracy on trained Set is (" + trainingRecords.size() +  ")" + accuracyTrainedData);
		System.out.println("\nAccuracy on test  Set :" + accuracyTestData);
	}

	class Record {

		public String datasetName = "";
		// An array of the parsed features in the data.
		public String[] attributeValue = new String[MAX_ATTRIBUTES];

		// A binary feature representing the output label of the dataset.
		public String outputLabel;

		// Define constructor

		public Record() {

		}

	}

	class Feature {
		public String name;
		public ArrayList<String> featureValues = new ArrayList<String>();
		public double entropy;
		public int MAX;

		public void setFeatureValues(ArrayList<Record> records, int index) {
			Record temp;
			for (int i = 0; i < records.size(); i++) {
				temp = records.get(i);
				if (!featureValues.contains(temp.attributeValue[index])) {
					featureValues.add(temp.attributeValue[index]);
				}
			}

		}

		public int getIndexOfAttributeValue(String attributeValue) {
			String temp;
			for (int i = 0; i < this.featureValues.size(); i++) {
				temp = this.featureValues.get(i);
				if (temp.equals(attributeValue)) {
					return i;
				}
			}
			// Something wrong if we reach here
			return -1;
		}

	}

	public boolean ReadFile(File fileName, ArrayList<Record> records,
			boolean isTrainingRecords, int trainingSet) {

		// Read the input file.
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(fileName);
		} catch (FileNotFoundException e) {
			
			return false;
		}

		// If the file was successfully opened, parse the file
		this.parse(fileScanner, records, isTrainingRecords, trainingSet);
		return true;
	}

	public void parse(Scanner fileScanner, ArrayList<Record> records,boolean trainingRecords, int trainingSet) {
		String line = fileScanner.nextLine().trim();
		int count = 0;
		int i =0;
		String token;
		StringTokenizer st = new StringTokenizer(line);
		ArrayList<Feature> tempAttribute = new ArrayList<Feature>();
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			Feature attribute = new Feature();
			if (trainingRecords) {
				attribute.name = token;
			}
			int maxValue = Integer.parseInt(st.nextToken());
			// update the MAX value for the feature attribute
			if (trainingRecords) {
				attribute.MAX = maxValue;
				// Add the attribute to the set of Atributes
				//System.out.println("Feature arraylist: name and MAX "+ attribute.name + " " + attribute.MAX);
				tempAttribute.add(attribute);
				attributes.add(tempAttribute);
				i++;
				
				count++;
			}
		}
		//System.out.println("attribute size" + attributes.size());
		for ( i =0; i < attributes.get(trainingSet).size(); i++)
		{
			Feature attribute;
			attribute = attributes.get(trainingSet).get(i);
			
		}
		// Set MAX_ATTRIBUTES while scanning training Records only
		if (trainingRecords)
			MAX_ATTRIBUTES = count;

		// Update the records till the end of the file
		while (fileScanner.hasNextLine()) {
			line = fileScanner.nextLine().trim();
			st = new StringTokenizer(line);
			 i = 0;
			String temp = null;
			Record r = hw1.new Record();
			count = 0;
			while (count < MAX_ATTRIBUTES) {
				temp = st.nextToken();
				// System.out.print(attribute + " ");
				r.attributeValue[count] = (temp);
				// System.out.print("testing");
				// System.out.print(r.attributeValue[i]+ "  ");
				count++;
			}
			// Update the records classLabel
			r.outputLabel = st.nextToken();

			// Add the record to the arraylist of Records
			records.add(r);
		}
		// Set the attribute values into Feature Arraylist
				for (i = 0; i < count; i++) {
					Feature attribute = attributes.get(trainingSet).get(i);
					attribute.setFeatureValues(Machine_Learning_Hw5.trainingRecords.get(trainingSet), i);
				}
				
				for ( i =0; i < count; i++)
				{
					Feature attribute = attributes.get(trainingSet).get(i);
					//System.out.println(" " +attribute.name + " " + attribute.MAX);
					//System.out.println("\nAttribute values for " + attribute.name);
					for ( int k =0; k < attribute.MAX; k++)
					{
						String temp = attribute.featureValues.get(k);
						//System.out.print(temp + " ");
					}
				}
				

	}

	// This is the node class which contains info like entropy, dataset, etc.

	public class Node {
		public Node parent;
		public Node[] children;
		public ArrayList<Record> data;
		public ArrayList<Feature> usedAttributes;
		public double entropy;
		public int noOfChildren;
		public boolean isTraversed;
		Feature attribute;
		String attributeValue;

		// private DiscreteAttribute testAttribute;

		public Node() {
			this.data = new ArrayList<Record>();
			this.usedAttributes = new ArrayList<Feature>();
			entropy = 0.0;
			parent = null;
			children = null;
			isTraversed = false;
			attribute = null;
			attributeValue = null;
			noOfChildren = 0;
		}

		public Node(ArrayList<Record> records) {
			this.data = new ArrayList<Record>();
			this.usedAttributes = new ArrayList<Feature>();
			this.data = records;
			entropy = 0.0;
			parent = null;
			children = null;
			isTraversed = false;
			attribute = null;
			attributeValue = null;
			noOfChildren = 0;
		}
		// setTestAttribute(new DiscreteAttribute("", 0));
		// public void setTestAttribute(DiscreteAttribute testAttribute) {
		// this.testAttribute = testAttribute;
	}

	// Class tree which consists of methods to create tree.

	public class Tree {
		public Node buildTree(ArrayList<Record> records, Node root, ArrayList<Feature> attributes, ArrayList<Feature> usedAttributes, int trainingSet) {

			//System.out.println("buildtree() called\n");
			int bestAttribute = -1;
			double bestGain = 0;
			Entropy newEntropy = new Entropy();
			root.entropy = newEntropy.calculateEntropy(root.data);

			if (root.entropy == 0) {
				//System.out.println("Root has entropy 0. Returning");
				return root;
			}
			
			//System.out.println("Max attribute is " + MAX_ATTRIBUTES);
			for (int i = 0; i < MAX_ATTRIBUTES; i++) {
				Feature newAttribute = attributes.get(i);
				// if( isAttributeUsed(newAttribute))
				// System.out.println("Attribute " + newAttribute.name +
				// " has been used already");
				if (!isAttributeUsed(newAttribute, root.usedAttributes)) {
				//	System.out.println("Testing: into the entropy calculation");
					double entropy = 0;
					ArrayList<Double> entropies = new ArrayList<Double>();
					ArrayList<Integer> setSizes = new ArrayList<Integer>();

					for (int j = 0; j < newAttribute.MAX; j++) {
						ArrayList<Record> subset = subset(root, i,
								newAttribute.featureValues.get(j), trainingSet);
						setSizes.add(subset.size());

						// If the subset record is not empty, Calculate the
						// entropy
						if (subset.size() != 0) {
							entropy = newEntropy.calculateEntropy(subset);
							entropies.add(entropy);
						}
					}

					// System.out.println("Entropy of a root  " + i + ": " +
					// root.entropy );
					double gain = newEntropy.calculateGain(root.entropy,
							entropies, setSizes, root.data.size());
					// System.out.println("Information gain :" + gain );
					// Update the bestgain and best Attribute
					if (gain > bestGain) {
						bestAttribute = i;
						bestGain = gain;
					}
				}
			}
			if (bestAttribute != -1) {
				int childSize = attributes.get(bestAttribute).MAX;
				Feature temp = attributes.get(bestAttribute);
				String attributeName = temp.name;
				//System.out.println("Best attribute is" + attributeName + "\n");
				// root.setTestAttribute(new
				// DiscreteAttribute(Hw1.attrMap.get(bestAttribute), 0));
				root.children = new Node[childSize];
				//root.isTraversed = true;
				root.noOfChildren = childSize;
				ArrayList<Feature> tempUsedAttributes
				= new ArrayList<Feature>();
				//Add the used attributes  of its parent
				for (int i =0; i < root.usedAttributes.size(); i++)
				{
					Feature tempAttr = root.usedAttributes.get(i);
					tempUsedAttributes.add(tempAttr);
				}
				//Now add the new attribute to the list of used attributes
				tempUsedAttributes.add(attributes.get(bestAttribute));
				// Create children for the node
				for (int j = 0; j < childSize; j++) {
					root.children[j] = new Node();
					root.children[j].parent = root;
					root.children[j].data = (subset(root, bestAttribute,
							temp.featureValues.get(j), trainingSet));
					root.children[j].attribute = temp;
					root.children[j].attributeValue = temp.featureValues.get(j);
					root.children[j].usedAttributes = tempUsedAttributes;
					// Need to verify if node needs leafname
				}
				//System.out.println("child Size is" + childSize);
				Node tempNode;
				for (int j = 0; j < childSize; j++) {
				//System.out.println("dividing based on " + root.children[j].attribute.name + " " + root.children[j].attributeValue);
					tempNode = buildTree(root.children[j].data, root.children[j],
							attributes, tempUsedAttributes, trainingSet);
				}

				// roots data == null??
				// root.data = null;
			} else {
				//System.out.println("No best attribute");
				return root;
			}

			return root;
		}

		public ArrayList<Record> subset(Node root, int attr, String value, int trainingSet) {
			ArrayList<Record> subset = new ArrayList<Record>();

			for (int i = 0; i < root.data.size(); i++) {
				Record record = root.data.get(i);

				if (record.attributeValue[attr].equals(value)) {
					subset.add(record);
				}
			}
			String tempName = attributes.get(trainingSet).get(attr).name;
			/*
			System.out.println("Records with attribute " + tempName
					+ "and value " + value);
			for (int x = 0; x < subset.size(); x++) {
				Record temp = subset.get(x);
				for (int i = 0; i < MAX_ATTRIBUTES; i++)
					System.out.print(temp.attributeValue[i] + " ");
				System.out.print(": " + temp.outputLabel + "\n");
			}*/
			return subset;
		}

		public boolean isAttributeUsed(Feature attribute,
				ArrayList<Feature> usedAttributeList) {
			if (usedAttributeList.contains(attribute)) {
				return true;
			} else {
				return false;
			}
		}

		public void printTree(Node root, int flag) {
			if (root == null) {
				//System.out.println("Root null");
				return;
			}
			//Print subsequent children
			if (!root.attribute.name.equals("first"))
			{
			// System.out.println("Root NOT null");
			System.out.print("\n  ");
			for (int i = 0; i < flag; i++) {
				System.out.print("| ");
			}
			System.out.print(root.attribute.name + " =" + root.attributeValue
					+ ":");
			flag++;
			}
			if (root.children == null) {
				String outputLabel = findLabel(root);
				System.out.print(outputLabel);
			}

			// Print children
			// System.out.print("children : " + root.noOfChildren );
			
			for (int i = 0; i < root.noOfChildren; i++) {
				printTree(root.children[i], flag);
			}

		}

		//This function is modified to send string which is nothing but the output label
		public int traverseTree(ArrayList<Node> roots, ArrayList<Record> records, int trainingSet) {
			
			//ArrayList consisiting of each model's classification
			ArrayList<String> classifications = new ArrayList<String>();
			int totalCount = 0;
			
			//System.out.println("\nCount records before " + count);
			for (int i = 0; i < records.size(); i++) {
				
				Record temp = records.get(i);
				for (int x =0; x < trainingSet; x++)
				{
					int trainingSetNumber= x;
				Queue<Node> queue = new LinkedList<Node>();
				String leafName = new String();
	 			int count = 0;
	 			Node root = roots.get(trainingSetNumber);
	 			queue.add(root);
				
				
				Node tempNode = root;
				// Traverse tree until we find the leaf node
				while (tempNode.children!= null ) {
					
					
					// Checking the attribute used to branch out the node
					Feature attr = tempNode.children[0].attribute;
					int j = getIndexOfAttribute(attr,trainingSet);

					if (j != -1) {
						// get the value of the attribute in the record
						String attrValue = temp.attributeValue[j];
						
						int k = attr.getIndexOfAttributeValue(attrValue);
						// Traverse to the specific child
						//System.out.println(" attr and attr value and k , jare" + k + " " + j + " "+attr.name + " "+ attrValue);
						tempNode = tempNode.children[k];
					} else if (j == -1) {
						System.out
								.println("Something wrong while traversing the tree");
						//Something wrong
						return -1;
					}
				
				}
				if (tempNode.children == null) {
					String leafname = findLabel(tempNode);
					leafName = leafname;
					
					classifications.add(leafName);
					
					//Need to specify the classification done by this training set here
					/*
					if (leafname.equals(temp.outputLabel)) {
						count++;
					}*/
				}
			}
				int voteCount0 = 0;
				int voteCount1 = 0;
				String finalLabel = null;
				for (int y = 0; y< classifications.size();y++)
					{
						String ClassifiedOutput = classifications.get(y);
						//System.out.println("outlabel from model " + y + ClassifiedOutput);
						if(ClassifiedOutput.equals("0"))
						{
							voteCount0++;
						}
						if(ClassifiedOutput.equals("1"))
						{
							voteCount1++;
						}
					}
				//Classify the final output based on voting of the training Sets
				
				if(voteCount0 > voteCount1)
				{
					finalLabel = "0";
				}
				else
				{
					finalLabel = "1";
				}
				
				//Check if output Label is correct or not
				if (finalLabel.equals(temp.outputLabel)) {
					totalCount++;
				}
				classifications.clear();
					
					
					

			}
			return totalCount;
		}

		public double findAccuracy(ArrayList<Node> roots, ArrayList<Record> record, int trainingSet) {
			double accuracy = 0.00;
			int count = 0;
			int size = record.size();

			//The following method call returns the total count which are correctly classified
			count = trees.get(0).traverseTree(roots, record, trainingSet);
			//System.out.println("Count records after traversing " + count);
			accuracy = ((double) count / (double) size) * 100;

			return accuracy;
		}

		public int getIndexOfAttribute(Feature attr, int trainingSet) {
			Feature temp;
			for (int i = 0; i < attributes.size(); i++) {
				temp = attributes.get(trainingSet).get(i);
				if (temp.name.equals(attr.name)) {
					return i;
				}
			}
			// Something wrong if we reach here
			return -1;
		}

		public String findLabel(Node root) {
			ArrayList<Record> record = root.data;
			Record temp;
			int countYes = 0;
			int countNo = 0;
			// Since it is a binary classification, we have only 2 values
			// Record testrecord = data.get(i);
			// Find out the count for all the records
			for (int j = 0; j < record.size(); j++) {
				temp = record.get(j);
				// If the output label matches 0 or 1 increase the count.
				if (temp.outputLabel.equals("0")) {
					countNo++;
				}
				if (temp.outputLabel.equals("1")) {
					countYes++;
				}
			}

			// Need to handle the equal condition
			if (countYes > countNo) {
				return "1";
			} else
				return "0";

		}
	}

	// Class to calculate Entropy, Information Gain etc.
	public class Entropy {
		public double calculateEntropy(ArrayList<Record> data) {
			double entropy = 0;

			if (data.size() == 0) {
				// nothing to do
				return 0;
			}

			// Since it is a binary classification, we have only 2 values
			for (int i = 0; i < 2; i++) {
				int count = 0;
				// Record testrecord = data.get(i);
				// Find out the count for all the records
				for (int j = 0; j < data.size(); j++) {
					Record record = data.get(j);
					// If the output label matches 0 or 1 increase the count.
					if (record.outputLabel.equals(Integer.toString(i))) {
						count++;
					}
				}

				double probability = count / (double) data.size();
				if (count > 0) {
					//System.out
					//		.println("Probability(" + i + "): " + probability);
					// needs to be verified for probaility 0 and 1.
					entropy += -probability
							* (Math.log(probability) / Math.log(2));
				}

				//System.out.println("Entropy: " + entropy);
			}

			return entropy;
		}

		public double calculateGain(double rootEntropy,
				ArrayList<Double> subEntropies, ArrayList<Integer> setSizes,
				int data) {
			double gain = rootEntropy;

			for (int i = 0; i < subEntropies.size(); i++) {
				gain += -((setSizes.get(i) / (double) data) * subEntropies
						.get(i));
			}

			return gain;
		}
	}

}
