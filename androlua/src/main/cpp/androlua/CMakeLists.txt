cmake_minimum_required(VERSION 3.6.0)

set(ANDROLUA_SOURCE_FILES
        src/androlua.cpp
        )

set(ANDROLUA_HEADER_FILES
        ../include/logger.h
        include/androlua.h
        )

find_library(log-lib log)
find_library(android-lib android)

add_library(androlua SHARED ${ANDROLUA_SOURCE_FILES})

target_include_directories(androlua PUBLIC ../include)
target_include_directories(androlua PUBLIC ../lua/src)
target_include_directories(androlua PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include)

target_link_libraries(androlua lua ${log-lib} ${android-lib})

