/**
 * 
 */

function convertRequirementPatternToTree(requirementPattern) {
	if (!requirementPattern.editable || !requirementPattern.available) { return null; }
	var node = {
			"data" : {
				"title" : requirementPattern.name,
			},
			//"state" : "closed",
			"attr" : {
				"rel" : "pattern",
//				"class" : "patternNode" ,
				"uri" : requirementPattern.uri
			},
//			"metadata" : {}
		};
	return node;
}

function convertInternalClassifierToTree(internalClassifier) {
	var node = {
		"data" : {
			"title" : internalClassifier.name,
			//"icon" : "folder"
		},
		//"state" : "closed",
		"attr" : { 
			"rel" : (internalClassifier.type==1) ? "basicClassifier" : "classifier"
		},
//		"metadata" : {},
		"children" : []
	};
	for (icNum in internalClassifier.internalClassifiers) {
		child = convertInternalClassifierToTree(internalClassifier.internalClassifiers[icNum]);
		node.children.push(child);
	}
	for (rpNum in internalClassifier.requirementPatterns) {
		child = convertRequirementPatternToTree(internalClassifier.requirementPatterns[rpNum]);
		node.children.push(child);
	}
	return node;
}

function convertRootClassifierToTree(rootClassifier) {
	var node = {
		"data" : {
			"title" : rootClassifier.name,
			//"icon" : "folder"
		},
		//"state" : "closed",
		"attr" : { "rel" : "classifier"},
//		"metadata" : {},
		"children" : []
	};
	for (icNum in rootClassifier.internalClassifiers) {
		child = convertInternalClassifierToTree(rootClassifier.internalClassifiers[icNum]);
		node.children.push(child);
	}
	return node;
}

function convertUnbindedPatternsToTree(unbindedPatterns) {
	var node = {
			"data" : {
				"title" : "Non-binded patterns",
				//"icon" : "folder"
			},
			//"state" : "closed",
			"attr" : { "rel" : "unbindedClassifier"},
//			"metadata" : {},
			"children" : []
		};
		for (rpNum in unbindedPatterns) {
			child = convertRequirementPatternToTree(unbindedPatterns[rpNum]);
			node.children.push(child);
		}
		return node;	
}

function loadPattern(patternUri) {
	$.ajax({
		url : patternUri,
		type : "GET",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		success: function(data,textStatus,jqXHR) {
			$("#selectedPatternInfo > :not(.template)").remove();
			$("#patternInfoTemplate.template").clone().appendTo($("#selectedPatternInfo")).attr("id","patternInfo").removeClass("template").fadeIn();
			
			$("#patternInfo #pt_name").text(data.name);
			$("#patternInfo #pt_description").text(data.description);
			$("#patternInfo #pt_comments").text(data.comments);
			$("#patternInfo #pt_goal").text(data.goal);
			$("#patternInfo #pt_author").text(data.author);
			
			$("#patternInfo #pt_sources").text("");
			for (scNum in data.sources) {
				$("#patternInfo #pt_sources").append($("<li></li>").text(data.sources[scNum].identifier));
			}
			
			$("#patternInfo #pt_keywords").text("");
			for (kwNum in data.keywords) {
				if (kwNum > 0) $("#patternInfo #pt_keywords").append(", ");
				$("#patternInfo #pt_keywords").append(data.keywords[kwNum]);
			}
			
			$("#patternInfo #pt_dependencies").text("");
			for (dpNum in data.dependencies) {
				if (dpNum > 0) $("#patternInfo #pt_dependencies").append("</br>");
				$("#patternInfo #pt_dependencies").append(data.dependencies[dpNum].type+" "+data.dependencies[dpNum].second.requirementPattern.name);
			}
			
			for (formNum in data.forms) {
				if (data.forms[formNum].available) {
					var form = $("#patternFormTemplate.template").children().clone().removeClass("template");
					var reqFormRows = parseInt(form.find(".pt_requirementForm").attr("rowspan"));
					var fixedPartRows = parseInt(form.find(".pt_fixedPart").attr("rowspan"));
					
					form.find(".pt_formName").text(data.forms[formNum].name);
					form.find(".pt_formDescription").text(data.forms[formNum].description);
					form.find(".pt_formComments").text(data.forms[formNum].comments);
					form.find(".pt_formModificationDate").text(data.forms[formNum].modificationDate);
					form.find(".pt_formAuthor").text(data.forms[formNum].author);
					
					form.find(".pt_formSources").text("");
					for (scNum in data.forms[formNum].sources) {
						form.find(".pt_formSources").append($("<li></li>").text(data.forms[formNum].sources[scNum].identifier));
					}
					
					form.find(".pt_formQuestionText").text(data.forms[formNum].fixedPart.questionText);
					form.find(".pt_formFormText").text(data.forms[formNum].fixedPart.formText);
					
					if (data.forms[formNum].fixedPart.parameters.length > 0) {
						$("#parameterTemplate.template .pt_formParameterHeaders").clone().appendTo(form);
						
						for (paramNum in data.forms[formNum].fixedPart.parameters) {
							var parameter = $("#parameterTemplate.template .pt_formParameter").clone();
							parameter.find(".pt_formParameterName").text(data.forms[formNum].fixedPart.parameters[paramNum].name+": "+data.forms[formNum].fixedPart.parameters[paramNum].description);
							parameter.find(".pt_formParameterMetric").text(data.forms[formNum].fixedPart.parameters[paramNum].metric.name+": "+data.forms[formNum].fixedPart.parameters[paramNum].metric.description);
							if ("setContent" in data.forms[formNum].fixedPart.parameters[paramNum].metric) {
								parameter.find(".pt_formParameterMetric").append("</br>"+data.forms[formNum].fixedPart.parameters[paramNum].metric.setContent.description);
							}
							parameter.appendTo(form);
						}
						reqFormRows+=(data.forms[formNum].fixedPart.parameters.length)+1;
						fixedPartRows+=(data.forms[formNum].fixedPart.parameters.length)+1;
					}
					form.find(".pt_fixedPart").attr("rowspan",fixedPartRows);
					
					if (data.forms[formNum].extendedParts.length > 0) {
						for (expNum in data.forms[formNum].extendedParts) {
							if (data.forms[formNum].extendedParts[expNum].available) {
								var extendedPart = $(".pt_extendedPartTemplate.template").children().clone().removeClass("template");
								var extendedPartRows = parseInt(extendedPart.find(".pt_extendedPart").attr("rowspan"));
								extendedPart.find(".pt_formQuestionText").text(data.forms[formNum].extendedParts[expNum].questionText);
								extendedPart.find(".pt_formFormText").text(data.forms[formNum].extendedParts[expNum].formText);
								if (data.forms[formNum].extendedParts[expNum].parameters.length > 0) {
									$("#parameterTemplate.template .pt_formParameterHeaders").clone().appendTo(extendedPart);
									for (paramNum in data.forms[formNum].extendedParts[expNum].parameters) {
										var parameter = $("#parameterTemplate.template .pt_formParameter").clone();
										parameter.find(".pt_formParameterName").text(data.forms[formNum].extendedParts[expNum].parameters[paramNum].name+": "+data.forms[formNum].extendedParts[expNum].parameters[paramNum].description);
										parameter.find(".pt_formParameterMetric").text(data.forms[formNum].extendedParts[expNum].parameters[paramNum].metric.name+": "+data.forms[formNum].extendedParts[expNum].parameters[paramNum].metric.description);
										if ("setContent" in data.forms[formNum].extendedParts[expNum].parameters[paramNum].metric) {
											parameter.find(".pt_formParameterMetric").append("</br>"+data.forms[formNum].extendedParts[expNum].parameters[paramNum].metric.setContent.description);
										}
										parameter.appendTo(extendedPart);
									}
									reqFormRows+=(data.forms[formNum].extendedParts[expNum].parameters.length)+1;
									extendedPartRows+=(data.forms[formNum].extendedParts[expNum].parameters.length)+1;
								}
								extendedPart.find(".pt_extendedPart").attr("rowspan",extendedPartRows);
								extendedPart.children().appendTo(form);
								reqFormRows+=2;
							}
						}
					}
					
					form.find(".pt_requirementForm").attr("rowspan",reqFormRows);
					form.children().appendTo($("#patternInfo")).fadeIn();
				}
			}
		}
	});
}

function loadSchema(schemaUri,unbinded) {
	unbinded = (typeof unbinded !== 'undefined') ? unbinded : true;
	
	//schemaUri+="?unbinded="+unbinded;
	
	$("#schemaNavigationTree").jstree({
		"core" : {
			"animation" : 250
		},
		"types" : {
			"types" : {
				"classifier" : {
					"icon" : {
						"image" : "Patterns_files/images/closeFolderSubChar.gif"
					},
					//"select_node" : false
				},
				"basicClassifier" : {
					"icon" : {
						"image" : "Patterns_files/images/closeFolder.gif"
					},
					//"select_node" : false
				},
				"pattern" : {
					"icon" : {
						"image" : "Patterns_files/images/leaveFolderChar.gif"
					}
				},
				"unbindedClassifier" : {
					"icon" : {
						"image" : "Patterns_files/images/closeFolderCharGray.gif"
					}
					//"select_node" : false
				},
			}
		},
		"json_data" : {
			"ajax" : {
				"url" : schemaUri,
				"data" : {unbinded: false, complete: true},
				"success" : function (schema) {
					tree = [];
					if ("rootClassifiers" in schema) {
						for(rcNum in schema.rootClassifiers) {
							node = convertRootClassifierToTree(schema.rootClassifiers[rcNum]);
							tree.push(node);
						}
					} else {
						for (rpNum in schema) {
							child = convertRequirementPatternToTree(schema[rpNum]);
							tree.push(child);
						}
					}
					if ("unbindedPatterns" in schema) {
						node = convertUnbindedPatternsToTree(schema.unbindedPatterns);
						tree.push(node);
					}
					return tree;
				}
			}
		},
		"plugins" : [ "themes", "json_data", "types", "ui" ]
	})
	.on("select_node.jstree", function (event, data) {
            if (data.rslt.obj.attr("rel") == "pattern") {
            	loadPattern(data.rslt.obj.attr("uri"));
            }
	});
}

function initHTMLElementsState() {
	$.ajax({
		url : apiURI+"/schemas",
		type : "GET",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		success: function(data,textStatus,jqXHR) {
			$("#schemaSelector").empty();
			for (schemaNum in data) {
				var option = $("<option>");
				option.val(data[schemaNum].uri).text(data[schemaNum].name);
				option.appendTo("#schemaSelector");
			}
			$("<option>").val(apiURI+"/patterns").text("Alphabetical list").appendTo("#schemaSelector");
			$("#schemaSelector").prop('selectedIndex',-1);
		}
	});
}

function initHTMLElementsBehaviour() {
	$("#schemaSelector").on("change",function(event) {
		loadSchema($("#schemaSelector").children(":selected").val(),false);
	});
}

jQuery(function($) {
	initHTMLElementsState();
	initHTMLElementsBehaviour();
});