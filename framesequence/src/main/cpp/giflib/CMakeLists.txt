cmake_minimum_required(VERSION 3.6.0)

#set(CMAKE_C_FLAGS "-Werror" ${CMAKE_C_FLAGS})
#set(CMAKE_C_FLAGS "-Wno-format" ${CMAKE_C_FLAGS})
#set(CMAKE_C_FLAGS "-Wno-sign-compare" ${CMAKE_C_FLAGS})
#set(CMAKE_C_FLAGS "-Wno-unused-parameter" ${CMAKE_C_FLAGS})
#set(CMAKE_C_FLAGS "-DHAVE_CONFIG_H" ${CMAKE_C_FLAGS})

set(GIF_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR})
include_directories(${GIF_SRC_DIR})
aux_source_directory(${GIF_SRC_DIR} LIB_GIF_SRC)
add_library(gif SHARED ${LIB_GIF_SRC})
target_link_libraries(gif)