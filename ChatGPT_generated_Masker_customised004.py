#
# Greg
# Hey dude,  generate python code that takes json file nested to an arbitrary depth and replaces  values with regex expressions - that the scriptgenerates itself  from the value being replaced =  except if the value is in a specific set of strings. Such values should remain as is.  And except for values of items with specific names contained in a  specific set of strings, say set "A". Such items should be given always the same value ( a string) apecified for each excluded name distinctly. And except for values of items with specific names contained in a  specific set of strings, say set "B". Such items should retain value as it is.
#
# ChatGPT:
#
def construct_generic_regex_pattern(input_string):
    # Escape special characters in the input string to create a valid regex pattern
    escaped_string = re.escape(input_string)
    #escaped_string = input_string
    
    # Replace verbatim digits with \d and verbatim letters with [a-zA-Z] in the escaped string
    generic_pattern = re.sub(r'[A-Za-z\s]+', r'[a-zA-Z\\s]+', escaped_string)
    # Replace digits with [0-9]+ or \d+
    generic_pattern = re.sub(r'[0-9]{9}', r'[0-9]{9}',generic_pattern )  # Replace digits with 9 digits
    generic_pattern = re.sub(r'[0-9]{4}', r'[0-9]{4}', generic_pattern)  # Replace digits with 4 digits
    generic_pattern = re.sub(r'[0-9]{3}', r'[0-9]{3}', generic_pattern)  # Replace digits with 4 digits
    generic_pattern = re.sub(r'[0-9]{2}', r'[0-9]{2}', generic_pattern) 
    
    
    # Constructing the regex pattern
    regex_pattern = f"^{generic_pattern}$"  # Matching the whole string
    
    return regex_pattern

import re
import json

def replace_values_with_regex(obj, exclude_set_values, specific_items_A, specific_items_B):
    if isinstance(obj, dict):
        for key, value in obj.items():
            if key in specific_items_A:
                obj[key] = specific_items_A[key]  # Replace specific items' values from set A
            elif key in specific_items_B:
                continue  # Retain values for specific items from set B
            elif isinstance(value, str) and value not in exclude_set_values:
                # Generate regex pattern based on the value being replaced
                regex_pattern = re.escape(value)  # Escaping special characters
                #regex = re.compile(regex_pattern)
                #obj[key] = regex.pattern  # Using regex pattern instead of the original value
                obj[key] = construct_generic_regex_pattern(regex_pattern)
            else:
                obj[key] = replace_values_with_regex(value, exclude_set_values, specific_items_A, specific_items_B)
        return obj
    elif isinstance(obj, list):
        return [replace_values_with_regex(elem, exclude_set_values, specific_items_A, specific_items_B) for elem in obj]
    else:
        return obj

# Read JSON file
file_path = 'article_regex_demo.json'
with open(file_path, 'r') as file:
    json_data = json.load(file)

# Define sets for excluded values, specific items from set A, and specific items from set B
exclude_set_values = {'developing-with-docker', 'sha1-IpnwLG3tMNSllhsLn3RSShj2NPw=', '1.3.7', 'FrontEndService','BackEndService','GET','SET', 'application/json','application/json; charset=utf-8','/products'}  # Replace with your excluded values
specific_items_A = {'id': 'uniqueGUID', 'integrity': 'integritySHA'}  # Replace with item names and replacements for set A
specific_items_B = {'version', 'resolved', 'media-typer', 'mime-types','statuses','productId', 'status' }  # Replace with item names for set B

# Process JSON data
processed_json = replace_values_with_regex(json_data, exclude_set_values, specific_items_A, specific_items_B)

# Display the processed JSON data
print(json.dumps(processed_json, indent=2))
