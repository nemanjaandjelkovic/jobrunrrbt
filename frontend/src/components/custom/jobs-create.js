import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import JobsCreateList from "./jobs-create-list";


const useStyles = makeStyles((theme) => ({
    paper: {
        padding: theme.spacing(3),
        whiteSpace: 'nowrap',
    }
}));

export default function JobsCreate() {
    const classes = useStyles();
    return (
        <div>
            <Box my={3}>
                <Typography variant="h4">Create Jobs</Typography>
            </Box>
            <Paper>
                <Box my={3} className={classes.paper}>
                    <JobsCreateList></JobsCreateList>
                </Box>
            </Paper>
        </div>
    )
}