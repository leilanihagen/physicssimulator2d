package src;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.google.protobuf.ExtensionRegistryLite;

import src.SP.Shape;
import src.SP.Snapshot;
import src.SP.Snapshots;

public class Main {

	public static Snapshots BuildSampleSnapshots() {
		Shape.Builder ballBuilder = Shape.newBuilder()
				.setType("circle")
				.setPosX(100.0)
				.setPosY(100.0)
				.setSizeX(30.0)
				.setSizeY(30.0);
		Shape ball1 = ballBuilder.build();
		Shape ball2 = ball1.toBuilder().setPosX(125).setPosY(140).build();
		Shape ball3 = ball1.toBuilder().setPosX(150).setPosY(180).build();
		List<Shape> shapeList = new ArrayList<>();
		shapeList.add(ball1);
		shapeList.add(ball2);
		shapeList.add(ball3);
		
		Snapshot.Builder snapshot1Builder = Snapshot.newBuilder()
				.setTime(0.0)
				.addAllShapes(shapeList);
		Snapshot snapshot1 = snapshot1Builder.build(); 
		
		Snapshots.Builder snapshotsBuilder = Snapshots.newBuilder()
				.addSnapshots(snapshot1);
		Snapshots snapshots = snapshotsBuilder.build();
		return snapshots;
	}
	
	public static void WriteSnapshots(Snapshots snapshots, String filePath)
			throws IOException {
		
		FileWriter fw = new FileWriter(filePath + ".ascii");
		fw.write(snapshots.toString());
		fw.close();
		
		OutputStream os = new FileOutputStream(filePath + ".binary");
		snapshots.writeTo(os);
		os.close();
	}
	
	public static Snapshots ReadSnapshots(String filePath)
			throws IOException {

		InputStream is = new FileInputStream(filePath + ".binary");
		ExtensionRegistryLite eReg = ExtensionRegistryLite.getEmptyRegistry();
		Snapshots snapshots = Snapshots.parseFrom(is, eReg);
		is.close();

		return snapshots;
	}
	
	public static void main(String[] args) throws IOException {

		Snapshots sample = BuildSampleSnapshots();
		WriteSnapshots(sample, "sample");
		Snapshots sampleRead = ReadSnapshots("sample");
		
		// Create frame:
		JFrame frame = new JFrame("Setup");
		// Size the frame:
		frame.setSize(600, 600);
		frame.setBackground(Color.blue);
		
		// Quit the program upon closing window:
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create components to put in the frame:
		DrawPanel drawPanel = new DrawPanel(sample, 0.0, 10);
		drawPanel.setLocation(300, 0);
		drawPanel.setSize(200, 200);
		drawPanel.setPreferredSize(new Dimension(200, 200));
		drawPanel.setVisible(true);
		frame.add(drawPanel);
		
//		JTextArea textArea = new JTextArea(5,20);
//		textArea.setBackground(Color.red);
//		textArea.setSize(200, 500);
//		textArea.setText("Hello world1\nHello world2\nHello world3");
//		textArea.setEditable(false);
//		JScrollPane scrollPane = new JScrollPane(textArea);
//		scrollPane.setBackground(Color.green);
//		scrollPane.setSize(250, 600);
//		scrollPane.setLocation(10, 10);
//		frame.add(scrollPane);
//		frame.pack();		
		
		// Display frame:
		frame.setVisible(true);
	}

}
