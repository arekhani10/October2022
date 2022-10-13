# Imports
import json
from PIL import Image, ImageDraw, ImageFont
import numpy as np


def plot_visualization(image_dict, image, pred_masks, location=None, saver=0):  # Write the required arguments
    # The function should plot the predicted segmentation maps and the bounding boxes on the images and save them.
    # Tip: keep the dimensions of the output image less than 800 to avoid RAM crashes.
    num_of_bboxes = len(image_dict['bboxes'])
    if num_of_bboxes > 3:
        num_of_bboxes = 3
        
    for j in image_dict['bboxes']:
        x_min, y_min, x_max, y_max = j['bbox']
        shape = [(x_min, y_min), (x_max, y_max)]
        drawer = ImageDraw.Draw(image)
        drawer.rectangle(shape, outline="black", width=5)  # draws boundary
        my_font = ImageFont.truetype('arial.ttf', 20)  # for bigger font
        drawer.text((x_min, y_min), j['category'], font=my_font, fill=(255, 255, 0))
        image_array = np.array(image)
        for masks in pred_masks:
            image_array = image_array + ((np.transpose(masks, (1, 2, 0))) * [0.2,0.1,0.5]*300)
        image = Image.fromarray(np.uint8(image_array)).convert("RGB")
    
        if saver == 1:
            file_object = open("./output/bboxes.txt","a")
            file_object.write("bbox coordinates: ( " + str(x_min) + " , " + str(y_min) + " ) and ( "  + str(x_max) + " , " + str(y_max) + " )\n")

    if saver == 1:
        image.save(location + image_dict['img_fn'].split('/')[-1])
        

    return image
