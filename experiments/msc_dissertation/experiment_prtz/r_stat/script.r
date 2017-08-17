library(ggplot2)
library(reshape2)

outFile <- "pval.tab"
unlink(outFile)

addRepCol <- function(test_in,norows,totrow){
  count=1;
  for( i in seq(1, totrow, by = norows) ){
    test_in[i:(i+norows-1),"rep"] <- count  
    count <- count+1
  }
  return(test_in)
}
  
## Gives count, mean, standard deviation, standard error of the mean, and confidence interval (default 95%).
##   data: a data frame.
##   measurevar: the name of a column that contains the variable to be summariezed
##   groupvars: a vector containing names of columns that contain grouping variables
##   na.rm: a boolean that indicates whether to ignore NA's
##   conf.interval: the percent range of the confidence interval (default is 95%)
summarySE <- function(data=NULL, measurevar, groupvars=NULL, na.rm=FALSE,
                      conf.interval=.95, .drop=TRUE) {
  library(plyr)
  
  # New version of length which can handle NA's: if na.rm==T, don't count them
  length2 <- function (x, na.rm=FALSE) {
    if (na.rm) sum(!is.na(x))
    else       length(x)
  }
  
  # This does the summary. For each group's data frame, return a vector with
  # N, mean, and sd
  datac <- ddply(data, groupvars, .drop=.drop,
                 .fun = function(xx, col) {
                   c(N    = length2(xx[[col]], na.rm=na.rm),
                     mean = mean   (xx[[col]], na.rm=na.rm),
                     sd   = sd     (xx[[col]], na.rm=na.rm)
                   )
                 },
                 measurevar
  )
  
  # Rename the "mean" column    
  datac <- rename(datac, c("mean" = measurevar))
  
  datac$se <- datac$sd / sqrt(datac$N)  # Calculate standard error of the mean
  
  # Confidence interval multiplier for standard error
  # Calculate t-statistic for confidence interval: 
  # e.g., if conf.interval is .95, use .975 (above/below), and use df=N-1
  ciMult <- qt(conf.interval/2 + .5, datac$N-1)
  datac$ci <- datac$se * ciMult
  
  return(datac)
}

write(paste("Policy","Method","Comparison","p-value",sep = "\t"),file=outFile,append=TRUE)

tab_test <- read.table("./out_test_frag.log", sep="\t", header=FALSE)
tab_test$V3 <- tab_test$V4
tab_test$V4 <- tab_test$V5
tab_test$V5 <- tab_test$V6
tab_test$V6 <- tab_test$V7
tab_test$V7 <- NULL
names(tab_test) <- c("policy", "mutants", "method", "Prioritization", "percent", "effectiveness")
tab_test$"testsuite" <- "set"



for(pol_id in unique(tab_test$"policy")){
  for(method_id in unique(tab_test$"method")){
    #message (paste(pol_id,method_id,sep=" ")) 
    test_cartax <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="cartax")),]
    test_damasc <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="damasc")),]
    test_random <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="random")),]
    test_random <- addRepCol(test_random,600,600)
    test_random <- aggregate(test_random[["effectiveness"]], by=list(test_random$policy, test_random$mutants, test_random$method, test_random$Prioritization, test_random$percent, test_random$testsuite, test_random$rep),  
                             function(x)mean(x, na.rm=TRUE))
    test_random <- test_random[,c(1,2,3,4,5,8,6)]
    names(test_random) <- c("policy", "mutants", "method","Prioritization", "percent", "effectiveness","testsuite")
    table_cols <- test_cartax
    table_cols$"Prioritization" <- NULL
    table_cols$"effectiveness" <- NULL
    table_cols$"cartax" <- test_cartax$"effectiveness"
    table_cols$"damasc" <- test_damasc$"effectiveness"
    table_cols$"random" <- test_random$"effectiveness"
    
    table_cols <- table_cols[,c(1,2,3,4,6,7,8,5)]
    
    summary_damasc <- summarySE(test_damasc, measurevar="effectiveness", groupvars=c("percent"))
    summary_damasc$Prioritization <- "RBAC"
    
    summary_cartax <- summarySE(test_cartax, measurevar="effectiveness", groupvars=c("percent"))
    summary_cartax$Prioritization <- "Simple"
    
    summary_random <- summarySE(test_random, measurevar="effectiveness", groupvars=c("percent"))
    summary_random$Prioritization <- "Random"
    
    summary <- rbind(summary_random,summary_cartax,summary_damasc)
    
    title_lab = paste(pol_id,toupper(method_id),sep=" - ")
    #print(filename)
    x_lab="Test Suite %"
    y_lab="% Effectiveness"
    leg_lab="Prioritization"
    
    # Standard error of the mean
    plot <- ggplot(summary, aes(x=percent, y=effectiveness,group=Prioritization,color=Prioritization)) +
      geom_errorbar(aes(ymin=effectiveness-ci, ymax=effectiveness+ci), width=.1) +
      geom_line() +
      geom_point(aes(shape=Prioritization, color=Prioritization))+
      scale_shape_manual(values=c(4,25,24))+
      #theme(panel.grid.major = element_line(colour = "darkgray")) + theme(panel.background = element_rect(fill = "white")) +
      theme_bw() +
      theme(plot.title = element_text(hjust = 0.5)) +
      scale_color_manual(values=c("#FF0000", "#00FF00" , "#0000FF"))+
      scale_y_continuous(limits=c(0, 1.0)) +
      scale_x_continuous(limits=c(0, 100)) +
      labs(title = title_lab, x = x_lab, y = y_lab, color = leg_lab)
    
    print(plot)
    filename <- paste(pol_id,method_id,"test.pdf",sep="_")
    ggsave(filename)
    
    x <- wilcox.test(table_cols$damasc,table_cols$cartax, paired=TRUE)
    write(paste(pol_id,method_id,"RBAC/Simple",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$cartax, alternative="g")
    
    x <- wilcox.test(table_cols$damasc,table_cols$random, paired=TRUE)
    write(paste(pol_id,method_id,"RBAC/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$random, alternative="g")
    
    x <- wilcox.test(table_cols$cartax,table_cols$random, paired=TRUE)
    write(paste(pol_id,method_id,"Simple/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$cartax-table_cols$random, alternative="g")
    
    #break
  }
  #break
}



tab_subset <- read.table("./out_subset_frag.log", sep="\t", header=FALSE) #tempcol <- data.frame(do.call('rbind', strsplit(as.character(tab_subset$V3),'.',fixed=TRUE)))
tab_subset$V3 <- data.frame(do.call('rbind', strsplit(as.character(tab_subset$V3),'.',fixed=TRUE)))$X2
tab_subset$V5 <- NULL
tab_subset$V6 <- NULL
names(tab_subset) <- c("policy", "mutants", "method","Prioritization", "percent", "effectiveness")
tab_subset$"testsuite" <- "subset"


for(pol_id in unique(tab_subset$"policy")){
  for(method_id in unique(tab_subset$"method")){
    #message (paste(pol_id,method_id,sep=" "))
    test_cartax <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="cartax")),]
    test_damasc <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="damasc")),]
    test_random <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="random")),]
    #break
    test_random <- addRepCol(test_random,200,6000)
    test_random <- aggregate(test_random[["effectiveness"]], by=list(test_random$policy, test_random$mutants, test_random$method, test_random$Prioritization, test_random$percent, test_random$testsuite, test_random$rep),
                             function(x)mean(x, na.rm=TRUE))
    test_random <- test_random[,c(1,2,3,4,5,8,6)]
    names(test_random) <- c("policy", "mutants", "method","Prioritization", "percent", "effectiveness","testsuite")
    #break
    table_cols <- test_cartax
    table_cols$"Prioritization" <- NULL
    table_cols$"effectiveness" <- NULL
    table_cols$"cartax" <- test_cartax$"effectiveness"
    table_cols$"damasc" <- test_damasc$"effectiveness"
    table_cols$"random" <- test_random$"effectiveness"

    table_cols <- table_cols[,c(1,2,3,4,6,7,8,5)]

    summary_damasc <- summarySE(test_damasc, measurevar="effectiveness", groupvars=c("percent"))
    summary_damasc$Prioritization <- "RBAC"

    summary_cartax <- summarySE(test_cartax, measurevar="effectiveness", groupvars=c("percent"))
    summary_cartax$Prioritization <- "Simple"

    summary_random <- summarySE(test_random, measurevar="effectiveness", groupvars=c("percent"))
    summary_random$Prioritization <- "Random"

    summary <- rbind(summary_random,summary_cartax,summary_damasc)

    summary$model <- factor(summary$model, levels=c("RBAC", "Random", "Simple"))
    
    
    title_lab = paste(pol_id,toupper(method_id),sep=" - ")
    #print(filename)
    x_lab="Test Suite %"
    y_lab="% Effectiveness"
    leg_lab="Prioritization"

    # Standard error of the mean
    plot <- ggplot(summary, aes(x=percent, y=effectiveness,group=Prioritization,color=Prioritization)) +
      geom_errorbar(aes(ymin=effectiveness-ci, ymax=effectiveness+ci), width=.1) +
      geom_line() +
      geom_point(aes(shape=Prioritization, color=Prioritization))+
      scale_shape_manual(values=c(4,25,24))+
      #theme(panel.grid.major = element_line(colour = "darkgray")) + theme(panel.background = element_rect(fill = "white")) +
      theme_bw() +
      theme(plot.title = element_text(hjust = 0.5)) +
      scale_color_manual(values=c("#FF0000", "#00FF00" , "#0000FF"))+
      scale_y_continuous(limits=c(0, 1.0)) +
      scale_x_continuous(limits=c(0, 100)) +
      labs(title = title_lab, x = x_lab, y = y_lab, color = leg_lab)

    print(plot)
    filename <- paste(pol_id,method_id,"test","subset_2528_test.pdf",sep="_")
    ggsave(filename)

    x <- wilcox.test(table_cols$damasc,table_cols$cartax, paired=TRUE)
    write(paste(pol_id,method_id,"RBAC/Simple",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$cartax, alternative="g")
    
    x <- wilcox.test(table_cols$damasc,table_cols$random, paired=TRUE)
    write(paste(pol_id,method_id,"RBAC/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$random, alternative="g")
    
    x <- wilcox.test(table_cols$cartax,table_cols$random, paired=TRUE)
    write(paste(pol_id,method_id,"Simple/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$cartax-table_cols$random, alternative="g")
    
    #break
  }
  #break
}
