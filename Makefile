# Common parameters
ARCHIVE := AlGraph.jar
MANIFEST := manifest.mf
BUILD_DIR := build
SRC_DIR := src

# Compiler
JCC := javac
SRC_EXTENSION := java
OBJ_EXTENSION := class
FLAGS := 

# OS specific staff
ifeq ($(OS), Windows_NT)
	SYSTEM := Windows
else
	SYSTEM := $(shell uname -s)
endif

# If on Windows
ifeq ($(SYSTEM),Windows)
	mkdir = @mkdir $(subst /,\\,$(1)) > nul 2>&1 || (exit 0)
	rm = rmdir $(1) /S /Q
endif

# If on Linux
ifeq ($(SYSTEM),Linux)
	mkdir = @mkdir $(1)
	rm = rm -rf $(1)
endif

#
# Implementation
#

# Recursive wildcard function
rwildcard = $(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))

# Find objects thanks to sources
SRCS := $(call rwildcard,$(SRC_DIR)/,*.$(SRC_EXTENSION))
OBJS := $(SRCS:$(SRC_DIR)/%.$(SRC_EXTENSION)=$(BUILD_DIR)/%.$(OBJ_EXTENSION))
ACT_MANIFEST := $(realpath $(MANIFEST))

# Set output dir and source dir
CFLAGS := -d $(BUILD_DIR) -sourcepath $(SRC_DIR)

all: $(BUILD_DIR)/$(ARCHIVE)

run: $(BUILD_DIR)/$(ARCHIVE)
	java -jar $<

# Create jar archive
$(BUILD_DIR)/$(ARCHIVE): $(dir $(OBJS)) $(OBJS)
	cd $(BUILD_DIR); jar cfm $(ARCHIVE) $(ACT_MANIFEST) *

# Create missing directories
%/:
	$(call mkdir,$@)

# Compile
$(BUILD_DIR)/%.$(OBJ_EXTENSION): $(SRC_DIR)/%.$(SRC_EXTENSION)
	$(JCC) $(CFLAGS) $(FLAGS) $<

# Cleanup
clean:
	$(call rm,$(BUILD_DIR))