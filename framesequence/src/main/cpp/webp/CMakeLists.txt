cmake_minimum_required(VERSION 3.6.0)

set(WEBP_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR})

include_directories(${WEBP_SRC_DIR})

aux_source_directory(${WEBP_SRC_DIR}/src/dec DEC_DIR)
aux_source_directory(${WEBP_SRC_DIR}/src/demux DEMUX_DIR)
aux_source_directory(${WEBP_SRC_DIR}/src/dsp DSP_DIR)
#aux_source_directory(${WEBP_SRC_DIR}/src/enc ENC_DIR)
#aux_source_directory(${WEBP_SRC_DIR}/src/mux MUX_DIR)
aux_source_directory(${WEBP_SRC_DIR}/src/utils UTILS_DIR)

add_library(webp-decode SHARED ${DEC_DIR} ${DEMUX_DIR} ${DSP_DIR} ${UTILS_DIR})
target_link_libraries(webp-decode)