cmake_minimum_required(VERSION 3.6.0)

add_compile_options(-DDLUA_DL_DLOPEN)
add_compile_options(-DLUA_COMPAT_5_1)
add_compile_options(-DLUA_COMPAT_5_2)

find_library(log-lib log)
find_library(android-lib android)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)

set(LUA_SRC_DIR ${SRC_DIR}/lua/src)
include_directories(${LUA_SRC_DIR})
aux_source_directory(${LUA_SRC_DIR} LUA_SRC)
list(REMOVE_ITEM LUA_SRC "${LUA_SRC_DIR}/lua.c")
list(REMOVE_ITEM LUA_SRC "${LUA_SRC_DIR}/luac.c")
add_library(lua SHARED ${LUA_SRC})
target_link_libraries(lua)

include_directories(${SRC_DIR}/includes)
add_library(
        androlua
        SHARED
        ${SRC_DIR}/androlua.cpp)

target_link_libraries(androlua ${log-lib} ${android-lib})