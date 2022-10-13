#Imports
from my_package.model import InstanceSegmentationModel
from my_package.data import Dataset
from my_package.analysis import plot_visualization
from my_package.data.transforms import FlipImage, RescaleImage, BlurImage, CropImage, RotateImage
import numpy as np
import json
from itertools import chain
import matplotlib.pyplot as plt
from PIL import Image


def experiment(annotation_file, segmentor, transforms, outputs):
    '''
        Function to perform the desired experiments

        Arguments:
        annotation_file: Path to annotation file
        segmentor: The image segmentor
        transforms: List of transformation classes
        outputs: path of the output folder to store the images
    '''
    
    # Create the instance of the dataset.
    dataset = Dataset(annotation_file,transforms)
    # Iterate over all data items.
    for i in range(len(dataset)):
        file_object = open("./output/bboxes.txt","a")
        file_object.write("Image " + str(i) + ": \n")
        image, image_dict = dataset[i]

        # Get the predictions from the segmentor.
        imageinarray = np.array(image)
        imageinarray = np.transpose(imageinarray,(2,0,1))
        imageinarray = imageinarray/255
        pred_boxes, pred_mask, pred_class, pred_score = segmentor(imageinarray)

        if len(pred_score) > 3:
            pred_score = pred_score[:3]
            pred_mask = pred_mask[:3]
            pred_class = pred_class[:3]
            pred_boxes = pred_boxes[:3]

        image_dict['bboxes'] = []
        for k in range(len(pred_score)):
            my_dict = {'bbox': list(chain.from_iterable(pred_boxes[k]))}
            for j in range(len(my_dict['bbox'])):
                my_dict['bbox'][j] = int(my_dict['bbox'][j])
            my_dict['category'] = pred_class[k]
            image_dict['bboxes'].append(my_dict)

        # Draw the segmentation maps on the image and save them.
        image = plot_visualization(image_dict=image_dict, image=image, location='./output/', saver=1, pred_masks=pred_mask)
        
    
    # Do the required analysis experiments.
    my_image = Image.open('./data/imgs/0.jpg')
    x, y = my_image.size

    # no transformation
    d = Dataset(annotation_file,transforms=[])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 1)
    plt.title('Original Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # horizontal flipping
    d = Dataset(annotation_file, transforms=[FlipImage()])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 2)
    plt.title('Horizontally Flipped Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # blurring
    d = Dataset(annotation_file, transforms=[BlurImage(radius=3)])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 3)
    plt.title('Blurred Image (Radius = 3)')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # 2x rescaling
    d = Dataset(annotation_file, transforms=[RescaleImage((2 * y, 2 * x))])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 4)
    plt.title('2x Rescaled Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # 0.5x rescaling
    d = Dataset(annotation_file, transforms=[RescaleImage((y // 2, x // 2))])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 5)
    plt.title('0.5x Rescaled Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # 90 degree rotation to right
    d = Dataset(annotation_file, transforms=[RotateImage(360 - 90)])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 6)
    plt.title('90 degree Rotated Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    # 45 degree rotation to left
    d = Dataset(annotation_file, transforms=[RotateImage(45)])
    my_image, my_image_dict = d[0]
    plt.subplot(2, 4, 7)
    plt.title('-45 degree Rotated Image')
    my_image = predictor(my_image, my_image_dict, segmentor)
    plt.imshow(my_image)

    plt.savefig('./output/AnalysisImg.jpg', bbox_inches='tight', pad_inches=0.1)


def predictor(image, image_dict, segmentor):
    image_dict['bboxes'] = []
    pred_boxes, pred_masks, pred_class, pred_score = segmentor(np.transpose(np.array(image), (2, 0, 1)) / 255)

    if len(pred_score) > 3:
        pred_score = pred_score[:3]
        pred_class = pred_class[:3]
        pred_boxes = pred_boxes[:3]
        pred_masks = pred_masks[:3]

    for k in range(len(pred_score)):
        my_dict = {'bbox': list(chain.from_iterable(pred_boxes[k]))}
        for j in range(len(my_dict['bbox'])):
            my_dict['bbox'][j] = int(my_dict['bbox'][j])
        my_dict['category'] = pred_class[k]
        image_dict['bboxes'].append(my_dict)
        
    return plot_visualization(image_dict=image_dict, image=image, pred_masks=pred_masks)


def main():
    segmentor = InstanceSegmentationModel()
    experiment('./data/annotations.jsonl', segmentor, [], None)  # Sample arguments to call experiment()


if __name__ == '__main__':
    main()
