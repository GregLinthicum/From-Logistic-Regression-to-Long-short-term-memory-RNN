import os
import json
from collections import defaultdict
import hashlib

# Get all JSON files in the current directory
json_files = [file for file in os.listdir() if file.endswith('.json')]

# Dictionary to store file content hashes
content_hashes = defaultdict(list)

# Collect hashes of file contents
for file_name in json_files:
    with open(file_name, 'r') as file:
        content = file.read()
        # Calculate hash of file content
        content_hash = hashlib.md5(content.encode()).hexdigest()
        # Store file name with the same content hash
        content_hashes[content_hash].append(file_name)

# Identify files with identical content
duplicates = {key: value for key, value in content_hashes.items() if len(value) > 1}

# Delete files with identical content, keeping only one copy
for files_list in duplicates.values():
    files_to_delete = files_list[1:]  # Keep the first file, delete the rest
    for file_name in files_to_delete:
        os.remove(file_name)
        print(f"File '{file_name}' deleted as it has identical content to other file(s).")