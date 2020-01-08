cmake_minimum_required(VERSION 3.6.0)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)

add_subdirectory(${SRC_DIR}/giflib)
add_subdirectory(${SRC_DIR}/webp)


# libframesequence
find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)

set(FRAMESEQUENCE_SRC_DIR ${SRC_DIR}/framesequence)
include_directories(${SRC_DIR}/giflib)
include_directories(${SRC_DIR}/webp/include)
include_directories(${FRAMESEQUENCE_SRC_DIR})
aux_source_directory(${FRAMESEQUENCE_SRC_DIR} FRAMESEQUENCE_SRC)
add_library(framesequence SHARED ${FRAMESEQUENCE_SRC})
target_link_libraries(framesequence gif webp-decode ${log-lib} ${jnigraphics-lib})