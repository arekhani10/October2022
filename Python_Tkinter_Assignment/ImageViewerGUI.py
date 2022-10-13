####### REQUIRED IMPORTS FROM THE PREVIOUS ASSIGNMENT #######
import tkinter
from urllib.parse import non_hierarchical
from matplotlib.image import BboxImage
from my_package.model import InstanceSegmentationModel
from my_package.data import Dataset
from my_package.analysis import plot_visualization
from my_package.data.transforms import FlipImage, RescaleImage, BlurImage, CropImage, RotateImage

####### ADD THE ADDITIONAL IMPORTS FOR THIS ASSIGNMENT HERE #######
from tkinter import *
from PIL import Image
from tkinter import ttk,filedialog
from tkinter.filedialog import askopenfile
from PIL import ImageTk
import numpy as np
from itertools import chain
from functools import partial

# Define the function you want to call when the filebrowser button is clicked.
def fileClick(clicked, dataset, segmentor):

	####### CODE REQUIRED (START) #######
	# This function should pop-up a dialog for the user to select an input image file.
	# Once the image is selected by the user, it should automatically get the corresponding outputs from the segmentor.
	# Hint: Call the segmentor from here, then compute the output images from using the `plot_visualization` function and save it as an image.
	# Once the output is computed it should be shown automatically based on choice the dropdown button is at.
	# To have a better clarity, please check out the sample video.
	imagelocation = filedialog.askopenfile(mode='r', title = "Select an image", filetypes=[('PNG files', '*.png'),('JGP files', '*.jpg')])
	if (imagelocation != None):
		e.delete(0,END)
		e.insert(0,imagelocation.name)
		global image
		image = Image.open(imagelocation.name).convert('RGB')
		ind = imagelocation.name.split("/")[-1].split(".")[0]
		#print(ind)
		imageinarray = np.array(image)
		imageinarray = np.transpose(imageinarray,(2,0,1))/255
		pred_boxes, pred_masks, pred_class, pred_score = segmentor(imageinarray)
		image_dict = dict()
		image_dict['bboxes'] = []
		for k in range(len(pred_score)):
			my_dict = {'bbox': list(chain.from_iterable(pred_boxes[k]))}
			for j in range(len(my_dict['bbox'])):
				my_dict['bbox'][j] = int(my_dict['bbox'][j])
			my_dict['category'] = pred_class[k]
			image_dict['bboxes'].append(my_dict)
		bboximage, segmentedimage = plot_visualization(image_dict = image_dict,image=image, pred_masks = pred_masks,saver=ind)
	####### CODE REQUIRED (END) #######

# `process` function definition starts from here.
# will process the output when clicked.
def process(clicked):

	####### CODE REQUIRED (START) #######
	# Should show the corresponding segmentation or bounding boxes over the input image wrt the choice provided.
	# Note: this function will just show the output, which should have been already computed in the `fileClick` function above.
	# Note: also you should handle the case if the user clicks on the `Process` button without selecting any image file.
	global ind
 
	if ind == None:
		print("Please select an image")
		return 
	
	segmentedimage = Image.open("segmented_images/" + str(ind) + ".jpg")
	bboximage = Image.open("bbox_images/" + str(ind) + ".jpg")
	if clicked == "Segmentation":
		input_label = Label(image=image)
		input_label.grid(row = 1, column = 0, columnspan = 2)
		output_label = Label(image=segmentedimage)
		output_label.grid(row = 1, column = 2, columnspan = 2)
	elif clicked == "Bounding Boxes":
		input_label = Label(image=image)
		input_label.grid(row = 1,column = 0, columnspan = 2)
		output_label = Label(image=bboximage)
		output_label.grid(row = 1, column = 2, columnspan = 2)	
	####### CODE REQUIRED (END) #######

# `main` function definition starts from here.
if __name__ == '__main__':

	####### CODE REQUIRED (START) ####### (2 lines)
	# Instantiate the root window.
	# Provide a title to the root window.
	root = Tk()
	root.title("Image Viewer GUI")
	
	####### CODE REQUIRED (END) #######

	# Setting up the segmentor model.
	annotation_file = './data/annotations.jsonl'
	transforms = []

	# Instantiate the segmentor model.
	segmentor = InstanceSegmentationModel()
	# Instantiate the dataset.
	dataset = Dataset(annotation_file, transforms=transforms)

	# Declare the options.
	options = ["Segmentation", "Bounding-box"]
	clicked = StringVar()
	clicked.set(options[0])

	e = Entry(root, width=70)
	e.grid(row=0, column=0)

	####### CODE REQUIRED (START) #######
	# Declare the file browsing button
	selectingImageButton = Button(root, text = "...", command = lambda: fileClick(clicked, dataset, segmentor))
	selectingImageButton.grid(row=0, column=1)
	####### CODE REQUIRED (END) #######

	####### CODE REQUIRED (START) #######
	# Declare the drop-down button
	dropdown = OptionMenu(root,clicked,*options)
	dropdown.grid(row=0, column=2)
	####### CODE REQUIRED (END) #######

	# This is a `Process` button, check out the sample video to know about its functionality
	myButton = Button(root, text="Process", command=partial(process, clicked))
	myButton.grid(row=0, column=3)

	####### CODE REQUIRED (START) ####### (1 line)
	# Execute with mainloop()
	root.mainloop()

	####### CODE REQUIRED (END) #######