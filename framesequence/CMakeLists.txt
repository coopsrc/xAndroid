cmake_minimum_required(VERSION 3.6.0)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)

add_subdirectory(${SRC_DIR}/giflib)
add_subdirectory(${SRC_DIR}/webp)


# libframesequence
find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)

set(JNI_SRC_DIR ${SRC_DIR}/jni)
include_directories(${SRC_DIR}/giflib)
include_directories(${SRC_DIR}/webp/include)
include_directories(${JNI_SRC_DIR})
aux_source_directory(${JNI_SRC_DIR} JNI_SRC)
add_library(framesequence SHARED ${JNI_SRC})
target_link_libraries(framesequence gif webp-decode ${log-lib} ${jnigraphics-lib})