cmake_minimum_required(VERSION 3.6.0)

find_library(log-lib log)
find_library(android-lib android)
find_library(graphics-lib jnigraphics)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)
aux_source_directory(${SRC_DIR} SOURCE_FILES)

add_library(
        dewdrops-blur
        SHARED
        ${SOURCE_FILES})

include_directories(${SRC_DIR}/includes)

target_link_libraries(dewdrops-blur
        ${log-lib} ${android-lib} ${graphics-lib})