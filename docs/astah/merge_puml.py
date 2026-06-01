import os
import re

def merge_puml_files():
    base_dir = "docs/details"
    if not os.path.exists(base_dir):
        print(f"Error: Base directory '{base_dir}' does not exist.")
        return

    # Find all usecase directories under docs/details
    uc_dirs = [d for d in os.listdir(base_dir) if os.path.isdir(os.path.join(base_dir, d))]
    uc_dirs.sort()

    print(f"Found {len(uc_dirs)} directories in '{base_dir}'. Starting merging process...")

    for uc_dir in uc_dirs:
        dir_path = os.path.join(base_dir, uc_dir)
        
        # Find all .puml files in this directory
        puml_files = [f for f in os.listdir(dir_path) if f.endswith(".puml")]
        
        # Filter out any already merged/generated files to prevent self-merging
        puml_files = [f for f in puml_files if not f.endswith("_all.puml") and not f.startswith("merged_")]
        puml_files.sort()

        if not puml_files:
            print(f"  [-] Directory '{uc_dir}' has no child .puml files. Skipping.")
            continue

        merged_file_name = f"{uc_dir}_all.puml"
        merged_file_path = os.path.join(dir_path, merged_file_name)
        
        print(f"  [+] Merging {len(puml_files)} files in '{uc_dir}' into '{merged_file_name}'...")
        
        merged_content = []
        merged_content.append(f"' ========================================== \n")
        merged_content.append(f"' MERGED PLANTUML DIAGRAMS FOR USE CASE: {uc_dir}\n")
        merged_content.append(f"' Total Diagrams: {len(puml_files)}\n")
        merged_content.append(f"' ========================================== \n\n")

        for puml_file in puml_files:
            file_path = os.path.join(dir_path, puml_file)
            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    content = f.read().strip()
                
                # Add headers and source file comments
                merged_content.append(f"' ------------------------------------------\n")
                merged_content.append(f"' Source File: {puml_file}\n")
                merged_content.append(f"' ------------------------------------------\n")
                merged_content.append(content)
                merged_content.append("\n\n")
            except Exception as e:
                print(f"    [!] Error reading file '{puml_file}': {e}")

        try:
            with open(merged_file_path, "w", encoding="utf-8") as f:
                f.write("".join(merged_content))
            print(f"    [Success] Created {merged_file_path}")
        except Exception as e:
            print(f"    [!] Error writing merged file for '{uc_dir}': {e}")

if __name__ == "__main__":
    merge_puml_files()
