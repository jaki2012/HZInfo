
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'echarts'], factory);
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('echarts'));
    } else {
        // Browser globals
        factory({}, root.echarts);
    }
}(this, function (exports, echarts) {
    var log = function (msg) {
        if (typeof console !== 'undefined') {
            console && console.error && console.error(msg);
        }
    };
    if (!echarts) {
        log('ECharts is not Loaded');
        return;
    }
    echarts.registerTheme('hnac', {
        "color": [
            "#0096ff",
            "#00a8ff",
            "#00baff",
            "#07bce8",
            "#04d6ec",
            "#02ebef"
        ],
        "backgroundColor": "rgba(255,255,255,0)",
        "textStyle": {},
        "title": {
            "textStyle": {
                "color": "#555555"
            },
            "subtextStyle": {
                "color": "#666666"
            }
        },
        "line": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "1.5"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": "1.5"
                }
            },
            "symbolSize": "5",
            "symbol": "circle",
            "smooth": false
        },
        "radar": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "1.5"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": "1.5"
                }
            },
            "symbolSize": "5",
            "symbol": "circle",
            "smooth": false
        },
        "bar": {
            "itemStyle": {
                "normal": {
                    "barBorderWidth": "0",
                    "barBorderColor": "#ffffff"
                },
                "emphasis": {
                    "barBorderWidth": "0",
                    "barBorderColor": "#ffffff"
                }
            }
        },
        "pie": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "scatter": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "boxplot": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "parallel": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "sankey": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "funnel": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "gauge": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            }
        },
        "candlestick": {
            "itemStyle": {
                "normal": {
                    "color": "#0c98e1",
                    "color0": "transparent",
                    "borderColor": "#0aa5e5",
                    "borderColor0": "#07bce8",
                    "borderWidth": "1"
                }
            }
        },
        "graph": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ffffff"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": "1",
                    "color": "#cccccc"
                }
            },
            "symbolSize": "5",
            "symbol": "circle",
            "smooth": false,
            "color": [
                "#0096ff",
                "#00a8ff",
                "#00baff",
                "#07bce8",
                "#04d6ec",
                "#02ebef"
            ],
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        },
        "map": {
            "itemStyle": {
                "normal": {
                    "areaColor": "#52ccff",
                    "borderColor": "#ffffff",
                    "borderWidth": "0.5"
                },
                "emphasis": {
                    "areaColor": "rgba(161,232,255,1)",
                    "borderColor": "#73e0ff",
                    "borderWidth": "0.5"
                }
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "rgb(41,41,41)"
                    }
                }
            }
        },
        "geo": {
            "itemStyle": {
                "normal": {
                    "areaColor": "#52ccff",
                    "borderColor": "#ffffff",
                    "borderWidth": "0.5"
                },
                "emphasis": {
                    "areaColor": "rgba(161,232,255,1)",
                    "borderColor": "#73e0ff",
                    "borderWidth": "0.5"
                }
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "rgb(41,41,41)"
                    }
                }
            }
        },
        "categoryAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "#d9d9d9"
                }
            },
            "axisTick": {
                "show": false,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "#808080"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "#eeeeee"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.05)",
                        "rgba(200,200,200,0.02)"
                    ]
                }
            }
        },
        "valueAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "#d9d9d9"
                }
            },
            "axisTick": {
                "show": false,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "#808080"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "#eeeeee"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.05)",
                        "rgba(200,200,200,0.02)"
                    ]
                }
            }
        },
        "logAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "#d9d9d9"
                }
            },
            "axisTick": {
                "show": false,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "#808080"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "#eeeeee"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.05)",
                        "rgba(200,200,200,0.02)"
                    ]
                }
            }
        },
        "timeAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "#d9d9d9"
                }
            },
            "axisTick": {
                "show": false,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "#808080"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "#eeeeee"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.05)",
                        "rgba(200,200,200,0.02)"
                    ]
                }
            }
        },
        "toolbox": {
            "iconStyle": {
                "normal": {
                    "borderColor": "#bdbdbd"
                },
                "emphasis": {
                    "borderColor": "#bfbfbf"
                }
            }
        },
        "legend": {
            "textStyle": {
                "color": "#808080"
            }
        },
        "tooltip": {
            "axisPointer": {
                "lineStyle": {
                    "color": "#242424",
                    "width": "0.5"
                },
                "crossStyle": {
                    "color": "#242424",
                    "width": "0.5"
                }
            }
        },
        "timeline": {
            "lineStyle": {
                "color": "#0e75df",
                "width": "1"
            },
            "itemStyle": {
                "normal": {
                    "color": "#b8b8b8",
                    "borderWidth": "1"
                },
                "emphasis": {
                    "color": "#0e75df"
                }
            },
            "controlStyle": {
                "normal": {
                    "color": "#0e75df",
                    "borderColor": "rgba(0,0,0,0)",
                    "borderWidth": "0.5"
                },
                "emphasis": {
                    "color": "#0e75df",
                    "borderColor": "rgba(0,0,0,0)",
                    "borderWidth": "0.5"
                }
            },
            "checkpointStyle": {
                "color": "#0e75df",
                "borderColor": "rgba(60,235,210,0.3)"
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#808080"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#808080"
                    }
                }
            }
        },
        "visualMap": {
            "color": [
                "#02ebef",
                "#04d6ec",
                "#07bce8",
                "#00baff",
                "#00a8ff",
                "#0096ff"
            ]
        },
        "dataZoom": {
            "backgroundColor": "rgba(255,255,255,0)",
            "dataBackgroundColor": "rgba(222,222,222,1)",
            "fillerColor": "#88dcff",
            "handleColor": "#00c2ff",
            "handleSize": "100%",
            "textStyle": {
                "color": "#808080"
            }
        },
        "markPoint": {
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        }
    });
}));
