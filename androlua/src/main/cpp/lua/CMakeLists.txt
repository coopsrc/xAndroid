cmake_minimum_required(VERSION 3.6.0)

set(LUA_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src)

aux_source_directory(${LUA_SRC_DIR} LUA_SOURCE_FILES)
list(REMOVE_ITEM LUA_SOURCE_FILES "${LUA_SRC_DIR}/lua.c")
list(REMOVE_ITEM LUA_SOURCE_FILES "${LUA_SRC_DIR}/luac.c")

add_library(lua SHARED ${LUA_SOURCE_FILES})

target_include_directories(lua PRIVATE ${LUA_SRC_DIR})

target_link_libraries(lua)