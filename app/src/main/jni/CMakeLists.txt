cmake_minimum_required(VERSION 3.28)
project(bicleaner)

set(CMAKE_CXX_SCAN_FOR_MODULES ON)

# 去除未使用函数与变量
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -ffunction-sections -fdata-sections")
set(CMAKE_CXX_FLAGS "${CMAKE_C_FLAGS}")
# 设置去除未使用代码的链接flag
#SET_TARGET_PROPERTIES(bicleaner PROPERTIES LINK_FLAGS "-Wl,--gc-sections")
#SET_TARGET_PROPERTIES(bicleaner PROPERTIES LINK_FLAGS "-Wl,--gc-sections,--icf=safe")


add_subdirectory(dex_builder)

add_library(${PROJECT_NAME} SHARED
        bicleaner.cc
)

target_link_libraries(${PROJECT_NAME} PUBLIC log dex_builder_static)

if (NOT DEFINED DEBUG_SYMBOLS_PATH)
    set(DEBUG_SYMBOLS_PATH ${CMAKE_BINARY_DIR}/symbols)
endif ()

add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E make_directory ${DEBUG_SYMBOLS_PATH}/${ANDROID_ABI}
        COMMAND ${CMAKE_OBJCOPY} --only-keep-debug $<TARGET_FILE:${PROJECT_NAME}>
        ${DEBUG_SYMBOLS_PATH}/${ANDROID_ABI}/${PROJECT_NAME}
        COMMAND ${CMAKE_STRIP} --strip-all $<TARGET_FILE:${PROJECT_NAME}>)
