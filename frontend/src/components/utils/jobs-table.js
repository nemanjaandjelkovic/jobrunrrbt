import React, {useEffect, useState} from 'react';
import {Link, useHistory, useLocation} from "react-router-dom";
import Typography from '@material-ui/core/Typography';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TablePagination from '@material-ui/core/TablePagination';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TimeAgo from "react-timeago/lib";
import {makeStyles} from '@material-ui/core/styles';
import LoadingIndicator from "../LoadingIndicator";
import Checkbox from "@material-ui/core/Checkbox";
import ButtonGroup from "@material-ui/core/ButtonGroup";
import Grid from "@material-ui/core/Grid";
import ReplayIcon from '@material-ui/icons/Replay';
import DeleteIcon from '@material-ui/icons/Delete';
import ActionButton from "../custom/actionButton";
import axios from "axios";


const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
        backgroundColor: theme.palette.background.paper,
    },
    content: {
        width: '100%',
    },
    table: {
        width: '100%',
    },
    noItemsFound: {
        padding: '1rem'
    },
    idColumn: {
        maxWidth: 0,
        width: '20%',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
    },
    jobNameColumn: {
        width: '60%'
    },
    inline: {
        display: 'inline',
    },
}));

const JobsTable = (props) => {
    const classes = useStyles();
    const location = useLocation();
    const history = useHistory();
    const isLoading = props.isLoading;
    const jobPage = props.jobPage;
    const jobState = props.jobState;
    const [rowsPerPage, setRowsPerPage] = React.useState(jobPage.limit);

    let column;
    let columnFunction = (job) => job.jobHistory[job.jobHistory.length - 1].createdAt;
    switch (jobState) {
        case 'SCHEDULED':
            column = "Scheduled";
            columnFunction = (job) => job.jobHistory[job.jobHistory.length - 1].scheduledAt;
            break;
        case 'ENQUEUED':
            column = "Enqueued";
            break;
        case 'PROCESSING':
            column = "Started";
            break;
        case 'SUCCEEDED':
            column = "Succeeded";
            break;
        case 'FAILED':
            column = "Failed";
            break;
        case 'DELETED':
            column = "Deleted";
            break;
        default:
    }

    const [isCheckAll, setIsCheckAll] = useState(false);
    const [isCheck, setIsCheck] = useState([]);
    const [list, setList] = useState([]);

    useEffect(() => {
        setList(jobPage.items);
    }, [list,jobPage.items]);

    const handleSelectAll = e => {
        setIsCheckAll(!isCheckAll);
        setIsCheck(list.map(li => li.id));
        if (isCheckAll) {
            setIsCheck([]);
        }
    };
    const handleClick = e => {
        const {checked} = e.target;
        setIsCheck([...isCheck, e.target.value]);
        if (!checked) {
            setIsCheck(isCheck.filter(item => item !== e.target.value));
        }
    };

    function deleteJob(id) {
        axios.delete(`/api/jobs/${id}`)
            .then(() => window.location.reload())
    }

    function requeueJob(id) {
        axios.post(`/api/jobs/${id}/requeue`)
            .then(() => window.location.reload())
    }

    function requeueJobs(listId) {
        console.log(listId)
        if (listId.length > 0) {
            listId.forEach((job) => {
                axios.post(`/api/jobs/${job}/requeue`)
            })
            setTimeout(() => {
                window.location.reload()
            }, "1000")
        }

    }

    function deleteJobs(listId) {
        if (listId.length > 0) {
            listId.forEach((job) => {
                axios.delete(`/api/jobs/${job}`)
            })
            setTimeout(() => {
                window.location.reload()
            }, "1000")
        }

    }

    const handleChangePage = (event, newPage) => {
        let urlSearchParams = new URLSearchParams(location.search);
        urlSearchParams.set("page", newPage);
        history.push(`?${urlSearchParams.toString()}`);
    };

    const handleChangeRowsPerPage = (event) => {
        jobPage.limit = parseInt(event.target.value);
        setRowsPerPage(parseInt(event.target.value));
        props.jobListLimit(event.target.value);
        if (parseInt(event.target.value) > jobPage.total && jobPage.currentPage !== 0) {
            let urlSearchParams = new URLSearchParams(location.search);
            urlSearchParams.set("page", 0);
            history.push(`?${urlSearchParams.toString()}`);
        }
    };

    return (
        <> {isLoading
            ? <LoadingIndicator/>
            : <> {jobPage.items < 1
                ? <Typography id="no-jobs-found-message" variant="body1" className={classes.noItemsFound}>No jobs
                    found</Typography>
                : <>
                    <TableContainer>
                        <Table id="jobs-table" className={classes.table} aria-label="jobs table">
                            <TableHead>
                                <TableRow>
                                    {jobState !== "DELETED" &&

                                        <TableCell>
                                            <Grid item xs="auto" md={4}>
                                                <Checkbox name="selectAll" id="selectAll" onChange={handleSelectAll}
                                                          checked={isCheckAll}/>
                                            </Grid>
                                        </TableCell>
                                    }
                                    <TableCell className={classes.idColumn}>Id</TableCell>
                                    <TableCell className={classes.jobNameColumn}>Job details</TableCell>
                                    <TableCell>{column}</TableCell>
                                    {jobState !== "DELETED" &&
                                        <TableCell>Actions</TableCell>
                                    }
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {jobPage.items.map(job => (
                                    <TableRow key={job.id}>
                                        {jobState !== "DELETED" &&
                                            <TableCell>
                                                <Checkbox key={job.id} value={job.id} id={job.id} onChange={handleClick}
                                                          checked={isCheck.includes(job.id)}/>
                                            </TableCell>
                                        }
                                        <TableCell component="th" scope="row" className={classes.idColumn}>
                                            <Link to={{
                                                pathname: `/dashboard/jobs/${job.id}`,
                                                job: job
                                            }}>{job.id}</Link>
                                        </TableCell>
                                        <TableCell>
                                            <Link to={{
                                                pathname: `/dashboard/jobs/${job.id}`,
                                                job: job
                                            }}>{job.jobName}</Link>
                                        </TableCell>
                                        <TableCell>
                                            <Link to={{pathname: `/dashboard/jobs/${job.id}`, job: job}}>
                                                <TimeAgo date={new Date(columnFunction(job))}
                                                         title={new Date(columnFunction(job)).toString()}/>
                                            </Link>
                                        </TableCell>
                                        {jobState !== "DELETED" &&
                                            <TableCell>
                                                <Grid item xs={4} container className={classes.jobDetails}>
                                                    <ButtonGroup>
                                                        {jobState !== "SCHEDULED" && jobState !== "SUCCEEDED" &&
                                                            <ActionButton value={job.id} background={"green"}
                                                                          color={"white"}
                                                                          Icon={<ReplayIcon/>} onClick={requeueJob}>
                                                            </ActionButton>
                                                        }
                                                        <ActionButton value={job.id} background={"red"}
                                                                      color={"white"}
                                                                      Icon={<DeleteIcon/>}
                                                                      onClick={deleteJob}></ActionButton>
                                                    </ButtonGroup>
                                                </Grid>
                                            </TableCell>
                                        }
                                    </TableRow>
                                ))}
                                {jobState !== "DELETED" &&
                                    <TableRow>
                                        <TableCell>
                                            <Grid item xs={4} container className={classes.jobDetails}>
                                                <ButtonGroup>
                                                    {jobState !== "SUCCEEDED" && jobState !== "SCHEDULED" &&
                                                    <ActionButton value={isCheck}
                                                                  background={"green"}
                                                                  color={"white"}
                                                                  Icon={<ReplayIcon/>}
                                                                  onClick={requeueJobs}></ActionButton>
                                                    }
                                                    <ActionButton value={isCheck} background={"red"}
                                                                  color={"white"}
                                                                  Icon={<DeleteIcon/>}
                                                                  onClick={deleteJobs}></ActionButton>
                                                </ButtonGroup>
                                            </Grid>
                                        </TableCell>
                                    </TableRow>
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        id="jobs-table-pagination"
                        component="div"
                        rowsPerPageOptions={[20,50,100]}
                        count={jobPage.total}
                        rowsPerPage={rowsPerPage}
                        page={!jobPage.total || jobPage.total <= 0 ? 0 : jobPage.currentPage}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                    />
                </>
            }
            </>
        }
        </>
    );
}

export default JobsTable;