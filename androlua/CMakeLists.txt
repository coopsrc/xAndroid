cmake_minimum_required(VERSION 3.6.0)

find_library(log-lib log)
find_library(android-lib android)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)

add_subdirectory(${SRC_DIR}/lua)

# libandrolua
set(ANDROLUA_SRC_DIR ${SRC_DIR}/androlua)

include_directories(${ANDROLUA_SRC_DIR})
include_directories(${SRC_DIR}/lua/src)
aux_source_directory(${ANDROLUA_SRC_DIR} ANDROLUA_SRC)
add_library(androlua SHARED ${ANDROLUA_SRC})
target_link_libraries(androlua lua ${log-lib} ${android-lib})