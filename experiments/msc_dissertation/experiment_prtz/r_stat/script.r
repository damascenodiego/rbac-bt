library(ggplot2)
library(reshape2)

outFile <- "pval.tab"
unlink(outFile)

tabData <- "data.tab"
unlink(tabData)

write(paste("\"policy\"","\"mutants\"","\"method\"","\"Prioritization\"","\"percent\"","\"testsuite\"","\"N\"","\"effectiveness\"","\"sd\"","\"se\"","\"ci\"",sep = "\t"),file=tabData)

addRepCol <- function(test_in,norows,totrow){
  count=1;
  for( i in seq(1, totrow, by = norows) ){
    if (test_in[i,"percent"]>=100) continue
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

tab_test100 <- read.table("./out_test_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- tab_test100$V6
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method", "effectiveness")
tab_test100$"Prioritization" <- "random"
tab_test100$"percent" <- "100"
tab_test100$"testsuite" <- "set"
tab_test100 <- tab_test100[,c(1,2,3,5,6,4,7)]
tab_test <- rbind(tab_test,tab_test100)


tab_test100 <- read.table("./out_test_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- tab_test100$V6
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method", "effectiveness")
tab_test100$"Prioritization" <- "cartax"
tab_test100$"percent" <- "100"
tab_test100$"testsuite" <- "set"
tab_test100 <- tab_test100[,c(1,2,3,5,6,4,7)]
tab_test <- rbind(tab_test,tab_test100)


tab_test100 <- read.table("./out_test_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- tab_test100$V6
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method", "effectiveness")
tab_test100$"Prioritization" <- "damasc"
tab_test100$"percent" <- "100"
tab_test100$"testsuite" <- "set"
tab_test100 <- tab_test100[,c(1,2,3,5,6,4,7)]
tab_test <- rbind(tab_test,tab_test100)

tab_test$percent <- as.numeric(tab_test$percent)

tab_test100 <- NULL

apfd_scenario <- character()
apfd_rbac <- numeric()
apfd_simple <- numeric()
apfd_random <- numeric()

apfd_tab <- data.frame(apfd_scenario,apfd_rbac,apfd_simple,apfd_random)
names(apfd_tab) <- c("Scenario", "APFD_RBAC", "APFD_Simple", "APFD_Random")

for(pol_id in unique(tab_test$"policy")){
  for(method_id in unique(tab_test$"method")){
    #message (paste(pol_id,method_id,sep=" "))
    test_cartax <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="cartax")),]
    test_damasc <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="damasc")),]
    test_random <- tab_test[((tab_test$"policy"==pol_id) & (tab_test$"method"==method_id) & (tab_test$"Prioritization"=="random")),]
    #break
    test_random <- addRepCol(test_random,600,600)
    
    #test_random <- aggregate(test_random[["effectiveness"]], by=list(test_random$policy, test_random$mutants, test_random$method, test_random$Prioritization, test_random$percent, test_random$testsuite, test_random$rep),
    #                         function(x)mean(x, na.rm=TRUE))
    #test_random <- test_random[,c(1,2,3,4,5,8,6)]
    # names(test_random) <- c("policy", "mutants", "method","Prioritization", "percent", "effectiveness","testsuite")
    # break
    
    summary_damasc <- summarySE(test_damasc, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_damasc$Prioritization <- "RBAC"
    
    summary_cartax <- summarySE(test_cartax, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_cartax$Prioritization <- "Simple"
    
    summary_random <- summarySE(test_random, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_random$Prioritization <- "Random"
    
    summary <- rbind(summary_random,summary_cartax,summary_damasc)
    
    table_cols <- summary_cartax
    table_cols$"Prioritization" <- NULL
    table_cols$"effectiveness" <- NULL
    table_cols$"N" <- NULL
    table_cols$"sd" <- NULL
    table_cols$"se" <- NULL
    table_cols$"ci" <- NULL
    table_cols$"cartax" <- summary_cartax$"effectiveness"
    table_cols$"damasc" <- summary_damasc$"effectiveness"
    table_cols$"random" <- summary_random$"effectiveness"
    
    
    table_cols <- table_cols[,c(1,2,3,4,6,7,8,5)]
    
    title_lab = paste(pol_id,toupper(method_id),sep=" - ")
    #print(filename)
    x_lab="Test Suite %"
    y_lab="% Effectiveness"
    leg_lab="Prioritization"
    
    # Standard error of the mean
    plot <- ggplot(summary, aes(x=percent, y=effectiveness,group=Prioritization,color=Prioritization)) +
      geom_errorbar(aes(ymin=effectiveness-ci, ymax=effectiveness+ci),color="black", width=1) +
      #geom_errorbar(aes(ymin=effectiveness-se, ymax=effectiveness+se),color="gray", width=1) +
      geom_line() +
      geom_point(aes(shape=Prioritization, color=Prioritization))+
      scale_shape_manual(values=c(4,25,24))+
      #theme(panel.grid.major = element_line(colour = "darkgray")) + theme(panel.background = element_rect(fill = "white")) +
      theme_bw() +
      theme(plot.title = element_text(hjust = 0.5),legend.justification=c(1,0),legend.position=c(0.975,0.15),legend.box.background = element_rect()) +
      scale_color_manual(values=c("#FF0000", "#00FF00" , "#0000FF"))+
      scale_y_continuous(limits=c(0, 1.0)) +
      scale_x_continuous(limits=c(0, 100),breaks=0:100*10) +
      labs(title = title_lab, x = x_lab, y = y_lab, color = leg_lab)
    
    print(plot)
    filename <- paste(pol_id,method_id,"test.png",sep="_")
    ggsave(filename)
    
    #write.table(summary,paste(pol_id,"_",method_id,".tab",sep=""),sep="\t",row.names=TRUE)
    write.table(summary,tabData,sep="\t",row.names=FALSE,col.names=FALSE,append = TRUE)
    
    x <- wilcox.test(table_cols$damasc,table_cols$cartax, paired=TRUE, alternative = "greater")
    write(paste(pol_id,method_id,"RBAC/Simple",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$cartax, alternative="g")
    
    x <- wilcox.test(table_cols$damasc,table_cols$random, paired=TRUE, alternative = "greater")
    write(paste(pol_id,method_id,"RBAC/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$random, alternative="g")
    
    # x <- wilcox.test(table_cols$cartax,table_cols$random, paired=TRUE, alternative = "greater")
    # write(paste(pol_id,method_id,"Simple/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    # #wilcox.test(table_cols$cartax-table_cols$random, alternative="g")
    
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

tab_test100 <- read.table("./out_subset_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- NULL
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method","testsuite", "effectiveness")
tab_test100$"Prioritization" <- "random"
tab_test100$"percent" <- "100"
tab_test100 <- tab_test100[,c(1,2,3,6,7,5,4)]
tab_subset <- rbind(tab_subset,tab_test100)


tab_test100 <- read.table("./out_subset_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- NULL
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method","testsuite", "effectiveness")
tab_test100$"Prioritization" <- "cartax"
tab_test100$"percent" <- "100"
tab_test100 <- tab_test100[,c(1,2,3,6,7,5,4)]
tab_subset <- rbind(tab_subset,tab_test100)


tab_test100 <- read.table("./out_subset_100.log", sep="\t", header=FALSE)
tab_test100$V3 <- tab_test100$V4
tab_test100$V4 <- tab_test100$V5
tab_test100$V5 <- NULL
tab_test100$V6 <- NULL
names(tab_test100) <- c("policy", "mutants", "method","testsuite", "effectiveness")
tab_test100$"Prioritization" <- "damasc"
tab_test100$"percent" <- "100"
tab_test100 <- tab_test100[,c(1,2,3,6,7,5,4)]
tab_subset <- rbind(tab_subset,tab_test100)

tab_subset$percent <- as.numeric(tab_subset$percent)

tab_test100 <- NULL

for(pol_id in unique(tab_subset$"policy")){
  for(method_id in unique(tab_subset$"method")){
    #message (paste(pol_id,method_id,sep=" "))
    test_cartax <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="cartax")),]
    test_damasc <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="damasc")),]
    test_random <- tab_subset[((tab_subset$"policy"==pol_id) & (tab_subset$"method"==method_id) & (tab_subset$"Prioritization"=="random")),]
    #break
    test_random <- addRepCol(test_random,200,6000)
    # test_random <- aggregate(test_random[["effectiveness"]], by=list(test_random$policy, test_random$mutants, test_random$method, test_random$Prioritization, test_random$percent, test_random$testsuite, test_random$rep),
    #                          function(x)mean(x, na.rm=TRUE))
    # test_random <- test_random[,c(1,2,3,4,5,8,6)]
    # names(test_random) <- c("policy", "mutants", "method","Prioritization", "percent", "effectiveness","testsuite")
    #break

    summary_damasc <- summarySE(test_damasc, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_damasc$Prioritization <- "RBAC"

    summary_cartax <- summarySE(test_cartax, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_cartax$Prioritization <- "Simple"

    summary_random <- summarySE(test_random, measurevar="effectiveness", groupvars=c("policy", "mutants", "method","Prioritization", "percent","testsuite"))
    summary_random$Prioritization <- "Random"

    summary <- rbind(summary_random,summary_cartax,summary_damasc)

    table_cols <- summary_cartax
    table_cols$"Prioritization" <- NULL
    table_cols$"effectiveness" <- NULL
    table_cols$"N" <- NULL
    table_cols$"sd" <- NULL
    table_cols$"se" <- NULL
    table_cols$"ci" <- NULL
    table_cols$"cartax" <- summary_cartax$"effectiveness"
    table_cols$"damasc" <- summary_damasc$"effectiveness"
    table_cols$"random" <- summary_random$"effectiveness"

    table_cols <- table_cols[,c(1,2,3,4,6,7,8,5)]

    title_lab = paste(pol_id,toupper(method_id),sep=" - ")
    #print(filename)
    x_lab="Test Suite %"
    y_lab="% Effectiveness"
    leg_lab="Prioritization"

    # Standard error of the mean
    plot <- ggplot(summary, aes(x=percent, y=effectiveness,group=Prioritization,color=Prioritization)) +
      geom_errorbar(aes(ymin=effectiveness-ci, ymax=effectiveness+ci),color="black", width=1) +
      #geom_errorbar(aes(ymin=effectiveness-se, ymax=effectiveness+se),color="gray", width=1) +
      geom_line() +
      geom_point(aes(shape=Prioritization, color=Prioritization))+
      scale_shape_manual(values=c(4,25,24))+
      #theme(panel.grid.major = element_line(colour = "darkgray")) + theme(panel.background = element_rect(fill = "white")) +
      theme_bw() +
      theme(plot.title = element_text(hjust = 0.5),legend.justification=c(1,0),legend.position=c(0.975,0.15),legend.box.background = element_rect()) +
      scale_color_manual(values=c("#FF0000", "#00FF00" , "#0000FF"))+
      scale_y_continuous(limits=c(0, 1.0)) +
      scale_x_continuous(limits=c(0, 100),breaks=0:100*10) +
      labs(title = title_lab, x = x_lab, y = y_lab, color = leg_lab)

    print(plot)
    filename <- paste(pol_id,method_id,"test","subset_2528_test.png",sep="_")
    ggsave(filename)

    #write.table(summary,paste(pol_id,"_",method_id,".tab",sep=""),sep="\t",row.names=TRUE)
    write.table(summary,tabData,sep="\t",row.names=FALSE,col.names=FALSE,append = TRUE)

    x <- wilcox.test(table_cols$damasc,table_cols$cartax, paired=TRUE, alternative = "greater")
    write(paste(pol_id,method_id,"RBAC/Simple",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$cartax, alternative="g")

    x <- wilcox.test(table_cols$damasc,table_cols$random, paired=TRUE, alternative = "greater")
    write(paste(pol_id,method_id,"RBAC/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    #wilcox.test(table_cols$damasc-table_cols$random, alternative="g")

    # x <- wilcox.test(table_cols$cartax,table_cols$random, paired=TRUE, alternative = "greater")
    # write(paste(pol_id,method_id,"Simple/Random",x$p.value,sep = "\t"),file=outFile,append=TRUE)
    # #wilcox.test(table_cols$cartax-table_cols$random, alternative="g")
    
    #break
  }
  #break
}
