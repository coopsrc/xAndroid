cmake_minimum_required(VERSION 3.6.0)

add_compile_options(-DDLUA_DL_DLOPEN)
add_compile_options(-DLUA_COMPAT_5_1)
add_compile_options(-DLUA_COMPAT_5_2)

set(LUA_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src)
include_directories(${LUA_SRC_DIR})
aux_source_directory(${LUA_SRC_DIR} LUA_SRC)
list(REMOVE_ITEM LUA_SRC "${LUA_SRC_DIR}/lua.c")
list(REMOVE_ITEM LUA_SRC "${LUA_SRC_DIR}/luac.c")
add_library(lua SHARED ${LUA_SRC})
target_link_libraries(lua)