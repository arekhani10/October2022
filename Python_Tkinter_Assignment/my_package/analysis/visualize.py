# Imports
import json
from PIL import Image, ImageDraw, ImageFont
import numpy as np


def plot_visualization(image_dict, image, pred_masks, saver, location=None):  # Write the required arguments
    # The function should plot the predicted segmentation maps and the bounding boxes on the images and save them.
    # Tip: keep the dimensions of the output image less than 800 to avoid RAM crashes.
    bboximage = image.copy()
    segmentationimage = image.copy()
    num_of_bboxes = len(image_dict['bboxes'])
    if num_of_bboxes > 3:
        num_of_bboxes = 3
        
    for j in image_dict['bboxes']:
        x_min, y_min, x_max, y_max = j['bbox']
        shape = [(x_min, y_min), (x_max, y_max)]
        drawer = ImageDraw.Draw(bboximage)
        drawer.rectangle(shape, outline="black", width=5)  # draws boundary
        my_font = ImageFont.truetype('arial.ttf', 20)  # for bigger font
        drawer.text((x_min, y_min), j['category'], font=my_font, fill=(255, 255, 0))
        
    image_array = np.array(segmentationimage)    
    for masks in pred_masks:
        image_array = image_array + ((np.transpose(masks, (1, 2, 0))) * [0,0,0.5]*300)
    
    #print("Hello")
    segmentationimage = Image.fromarray(np.uint8(image_array)).convert("RGB")
    segmentationimage.save("./segmented_images/" + str(saver) + ".jpg")
    bboximage.save("./bbox_images/" + str(saver) + ".jpg")
    return bboximage,segmentationimage
